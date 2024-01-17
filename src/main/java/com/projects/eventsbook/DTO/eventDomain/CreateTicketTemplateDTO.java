package com.projects.eventsbook.DTO.eventDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketTemplateDTO {
    private Long eventId;
    private Double price;
    private Integer ticketsCount;
    private String description;
}
