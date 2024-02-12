package com.projects.eventsbook.service.event;

import com.projects.eventsbook.DAO.BoughtTicketRepositoryJPA;
import com.projects.eventsbook.DAO.EventRepositoryJPA;
import com.projects.eventsbook.DAO.GroupRepositoryJpa;
import com.projects.eventsbook.entity.*;
import com.projects.eventsbook.enumerations.GroupRole;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.util.RetrieveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{
    private final BoughtTicketRepositoryJPA boughtTicketRepository;
    private final EventRepositoryJPA eventRepository;
    private final GroupRepositoryJpa groupRepositoryJpa;

    @Autowired
    public AuthorizationServiceImpl(BoughtTicketRepositoryJPA boughtTicketRepository, EventRepositoryJPA eventRepository, GroupRepositoryJpa groupRepositoryJpa) {
        this.boughtTicketRepository = boughtTicketRepository;
        this.eventRepository = eventRepository;
        this.groupRepositoryJpa = groupRepositoryJpa;
    }

    @Override
    public boolean canUserVisitEvent(Event eventToVisit, User user) {
        return !eventToVisit.getEventGroup().getIsPrivate();
    }

    @Override
    public boolean canUserVisitGroup(EventGroup eventGroup, User user) {
        return !eventGroup.getIsPrivate() ||
                eventGroup
                .getMembers()
                .stream()
                .noneMatch(groupMember -> groupMember.getUser().getId().equals(user.getId()));
    }

    @Override
    public void validateChangeGroupRole(GroupMember actor, GroupMember toBeChanged, GroupRole role)
            throws InvalidOperationException {
        GroupRole actorRole = actor.getGroupRole();
        GroupRole toBeChangedRole = toBeChanged.getGroupRole();
        if (toBeChangedRole.equals(GroupRole.OWNER)) {
            throw new InvalidOperationException("Owner cannot be modified!");
        }
        if (!actorRole.equals(GroupRole.OWNER)) {
            throw new InvalidOperationException("Only owner can modify roles!");
        }
    }

    @Override
    public void validateManageMembershipRequest(GroupMember actor) {
        GroupRole actorGroupRole = actor.getGroupRole();
        if (actorGroupRole.equals(GroupRole.VIEWER)) {
            throw new InvalidOperationException("Viewer cannot decide a membership request!");
        }
    }

    @Override
    public boolean canMemberModifyGroup(GroupMember groupMember) {
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean canUserMakeReviewToEvent(User user, Event event) {
        boolean isEventUpcoming = event.getEndTime().isAfter(LocalDateTime.now());
        if (isEventUpcoming) {
            return false;
        }

        List<BoughtTicket> userTickets = this.boughtTicketRepository.findAllByBoughtBy_Id(user.getId());
        boolean hasUserBoughtTicket = userTickets.stream()
                .anyMatch(boughtTicket -> boughtTicket
                        .getTicketTemplate()
                        .getEvent().equals(event));

        if (!hasUserBoughtTicket) {
            return false;
        }

        boolean hasUserAlreadyMadeReview = event.getReviews()
                .stream()
                .anyMatch(review -> review
                        .getUser().equals(user));
        return !hasUserAlreadyMadeReview;
    }

    @Override
    public void validateAccessEventAdminPanel(Long eventId, Long userId) {
        Event event = RetrieveUtil.getByIdWithException(eventRepository, eventId);
        EventGroup group = RetrieveUtil.getByIdWithException(groupRepositoryJpa, event.getEventGroup().getId());
        Optional<GroupMember> groupMember = group
                .getMembers()
                .stream()
                .filter(member -> member.getUser().getId().equals(userId))
                .findFirst();
        if (groupMember.isEmpty()) {
            throw new NoEntityFoundException("The group has no such member");
        }
        if (canMemberModifyGroup(groupMember.get())) {
            throw new InvalidOperationException("No needed permissions");
        };
    }
}
