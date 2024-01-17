package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DTO.groupDomain.CreateGroupDTO;

public interface EventGroupValidator {
    public void validateName(String name);
    public void validateDescription(String description);

    public void validateCreateGroup(CreateGroupDTO createGroupDTO);
}
