package com.projects.eventsbook.service.event;

import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.enumerations.GroupRole;

public interface AuthorizationService {
    public boolean canUserVisitEvent(Event eventToVisit, User user);
    public boolean canUserVisitGroup(EventGroup eventGroup, User user);
    public void validateChangeGroupRole(GroupMember actor, GroupMember toBeChanged, GroupRole role);
    public void validateManageMembershipRequest(GroupMember actor);

    public boolean canMemberModifyGroup(GroupMember groupMember); // To do
    public boolean canUserMakeReviewToEvent(User user, Event event); // To do

    public void validateAccessEventAdminPanel(Long userId, Long eventId);
}
