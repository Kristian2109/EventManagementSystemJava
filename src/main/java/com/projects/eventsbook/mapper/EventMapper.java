package com.projects.eventsbook.mapper;

import com.projects.eventsbook.DTO.eventDomain.CreateEventDTO;
import com.projects.eventsbook.entity.Event;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class EventMapper {

    public static Event toEvent(CreateEventDTO createEventDTO) {
        Event event = new Event();
        BeanUtils.copyProperties(createEventDTO, event);
        String[] tags = createEventDTO.getTags().split(",");
        event.setTags(Arrays.stream(tags).toList());
        return event;
    }
}
