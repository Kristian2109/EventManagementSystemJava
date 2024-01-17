package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DTO.groupDomain.CreateGroupDTO;
import org.springframework.stereotype.Service;

@Service
public class EventGroupValidatorImpl implements EventGroupValidator{
    @Override
    public void validateName(String name) {

    }

    @Override
    public void validateDescription(String description) {

    }

    @Override
    public void validateCreateGroup(CreateGroupDTO createGroupDTO) {
        validateName(createGroupDTO.getName());
        validateDescription(createGroupDTO.getDescription());
    }
}
