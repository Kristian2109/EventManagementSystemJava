package com.projects.eventsbook.mapper;

import com.projects.eventsbook.DTO.eventDomain.CreateEventDTO;
import com.projects.eventsbook.DTO.eventDomain.EventDetails;
import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.Review;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

public class EventMapper {

    public static Event toEvent(CreateEventDTO createEventDTO) {
        Event event = new Event();
        BeanUtils.copyProperties(createEventDTO, event);
        String[] tags = createEventDTO.getTags().split(",");
        event.setTags(Arrays.stream(tags).toList());
        return event;
    }

    public static EventDetails toEventDetails(Event event) {
        EventDetails eventDetails = new EventDetails();
        BeanUtils.copyProperties(event, eventDetails);
        eventDetails.setCreatedById(event.getCreatedBy().getId());
        eventDetails.setCreatedByName(event.getCreatedBy().getUsername());
        eventDetails.setEventGroupId(event.getEventGroup().getId());
        eventDetails.setReviewsCount((long) event.getReviews().size());
        eventDetails.setAverageRating(event
                .getReviews()
                .stream()
                .map(Review::getRating)
                .reduce(0, Integer::sum)
                / (double) eventDetails.getReviewsCount()
        );

        eventDetails.setTicketTemplates(
                event.getTicketTemplates()
                        .stream()
                        .map(TicketMapper::toTicketTemplateDetails)
                        .collect(Collectors.toList()));
        eventDetails.setImage(event.getImage());
        return eventDetails;
    }
}
