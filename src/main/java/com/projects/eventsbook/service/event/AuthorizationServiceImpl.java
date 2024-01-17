package com.projects.eventsbook.service.event;

import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.User;

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
}
