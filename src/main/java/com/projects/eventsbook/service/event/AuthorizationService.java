package com.projects.eventsbook.service.event;

import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.User;

public interface AuthorizationService {
    public boolean canUserVisitEvent(Event eventToVisit, User user);
    public boolean canUserVisitGroup(EventGroup eventGroup, User user);
}
