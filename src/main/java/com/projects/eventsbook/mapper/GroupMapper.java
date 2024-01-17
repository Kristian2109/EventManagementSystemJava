package com.projects.eventsbook.mapper;

import com.projects.eventsbook.DTO.groupDomain.CreateGroupDTO;
import com.projects.eventsbook.DTO.groupDomain.UpdateGroupDTO;
import com.projects.eventsbook.DTO.UserGroupDTO;
import com.projects.eventsbook.entity.EventGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class GroupMapper {

    public static UserGroupDTO toUserGroupDTO(EventGroup eventGroup) {
        return new UserGroupDTO(eventGroup.getId(), eventGroup.getName());
    }

    public static EventGroup toGroup (CreateGroupDTO eventGroup) {
        EventGroup group = new EventGroup();
        BeanUtils.copyProperties(eventGroup, group);
        return group;
    }

    public static void merge(UpdateGroupDTO updateGroupDTO, EventGroup eventGroup) {
        BeanUtils.copyProperties(updateGroupDTO, eventGroup);
    }
}
