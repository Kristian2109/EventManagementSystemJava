package com.projects.eventsbook.service.event;

import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.enumerations.GroupRole;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{
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


}
