package com.projects.eventsbook.service.event;

import com.projects.eventsbook.DAO.EventRepositoryJPA;
import com.projects.eventsbook.DTO.eventDomain.CreateEventDTO;
import com.projects.eventsbook.DTO.eventDomain.CreateReviewDTO;
import com.projects.eventsbook.DTO.eventDomain.CreateTicketTemplateDTO;
import com.projects.eventsbook.entity.*;
import com.projects.eventsbook.enumerations.GroupMemberStatus;
import com.projects.eventsbook.enumerations.GroupRole;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.service.FileService;
import com.projects.eventsbook.service.eventGroup.EventGroupService;
import com.projects.eventsbook.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepositoryJPA eventRepositoryJPA;
    private final UserService userService;
    private final EventGroupService eventGroupService;
    private final FileService fileService;

    @Autowired
    public EventServiceImpl(EventRepositoryJPA eventRepositoryJPA, UserService userService, EventGroupService eventGroupService, FileService fileService) {
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.userService = userService;
        this.eventGroupService = eventGroupService;
        this.fileService = fileService;
    }


    @Override
    public Event create(CreateEventDTO createEventDTO) {
        User currentUser = userService.getById(createEventDTO.getCreatedById());
        EventGroup currentEventGroup = eventGroupService.get(createEventDTO.getEventGroupId());
        checkUserCreateEventInGroup(currentUser, currentEventGroup);

        Event eventToCreate = new Event();
        eventToCreate.setCreatedBy(currentUser);
        eventToCreate.setEventGroup(currentEventGroup);
        eventToCreate.setImageFile(fileService.getFileById(createEventDTO.getImageDataId()));
        BeanUtils.copyProperties(createEventDTO, eventToCreate);

        return eventRepositoryJPA.save(eventToCreate);
    }

    @Override
    public Event getById(Long eventId) {
        Optional<Event> event = eventRepositoryJPA.findById(eventId);
        if (event.isEmpty()) {
            throw new NoEntityFoundException("No event found");
        }
        return event.get();
    }

    @Override
    public void addTicket(CreateTicketTemplateDTO createTicketTemplateDTO) {
        Event event = getById(createTicketTemplateDTO.getEventId());
        TicketTemplate newTicketTemplate = new TicketTemplate();
        BeanUtils.copyProperties(createTicketTemplateDTO, newTicketTemplate);
        newTicketTemplate.setCurrentTicketsCount(createTicketTemplateDTO.getTicketsCount());
        newTicketTemplate.setInitialTicketsCount(createTicketTemplateDTO.getTicketsCount());
        newTicketTemplate.setEvent(event);
        event.addTicketTemplate(newTicketTemplate);
        eventRepositoryJPA.save(event);
    }

    @Override
    public List<Event> getBrowsingEventsForUser(User user, int pageNumber, int pageSize) {
        List<Event> events = eventRepositoryJPA.findAll();


        return null;
    }

    @Override
    public Review makeReviewToEvent(CreateReviewDTO createReviewDTO) {
        Event event = getById(createReviewDTO.getEventId());
        User user = userService.getById(createReviewDTO.getReviewerId());
//        if (!user.hasUserBeenOnEvent(event)) {
//            throw new InvalidOperationException("User should participate in an event to make a review.");
//        }
        Review review = new Review();
        BeanUtils.copyProperties(createReviewDTO, review);
        review.setEvent(event);
        review.setUser(user);
        event.addEventReview(review);
        eventRepositoryJPA.save(event);
        return review;
    }

    @Override
    public boolean canUserReviewEvent(Long userId, Long eventId) {
        return eventRepositoryJPA.userReviewsCount(userId, eventId) == 0;
    }

    private void checkUserCreateEventInGroup(User user, EventGroup eventGroup) {
        Optional<GroupMember> member = eventGroup
                .getMembers()
                .stream()
                .filter(groupMember -> groupMember.getUser().getId().equals(user.getId()) &&
                                        groupMember.getStatus().equals(GroupMemberStatus.ACCEPTED))
                .findFirst();

        if (member.isEmpty()) {
            throw new InvalidOperationException("User isn't member of the group");
        }
        if (!member.get().getGroupRole().equals(GroupRole.ADMIN)) {
            throw new InvalidOperationException("User isn't an admin of the group");
        }
    }
}
