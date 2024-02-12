package com.projects.eventsbook.DTO.eventDomain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class EventDetails {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime beginTime;
    LocalDateTime endTime;
    String name;
    String description;
    Boolean isFree;
    Boolean isOnline;
    Long eventGroupId;
    Long createdById;
    String createdByName;
    String image;
    List<String> tags;
    Long reviewsCount;
    Double averageRating;
    List<TicketTemplateDetails> ticketTemplates;


    public EventDetails() {};
}
