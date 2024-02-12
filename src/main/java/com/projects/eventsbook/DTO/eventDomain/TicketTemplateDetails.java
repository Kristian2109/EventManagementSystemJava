package com.projects.eventsbook.DTO.eventDomain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketTemplateDetails{
        Long id;
        Double price;
        Integer initialTicketsCount;
        String description;
        Integer currentTicketsCount;
        Double revenue;

}
