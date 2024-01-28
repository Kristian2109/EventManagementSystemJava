package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DTO.groupDomain.CreateGroupDTO;
import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.DTO.groupDomain.UpdateGroupDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.enumerations.GroupRole;

import java.util.List;

public interface EventGroupService {
    public EventGroup create(CreateGroupDTO createGroupDTO);
    public Long delete(Long id);
    public EventGroup get(Long id);
    public EventGroup update(UpdateGroupDTO updateGroupDTO);
    public List<EventGroup> getEventGroupsByPage(Long userId, int pageSize, int pageNumber);
    public List<EventGroup> getUserGroups(Long userId);
    public List<EventGroup> getGroupsForUserToJoin(Long userId);
}
