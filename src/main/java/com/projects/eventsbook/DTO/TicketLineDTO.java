package com.projects.eventsbook.DTO;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketLineDTO {
    private Long id;
    private Long eventId;
    private String eventName;
    private LocalDateTime eventTimeBegin;
    private LocalDateTime eventTimeEnd;
    private Integer ticketsCount;
    private Double ticketPrice;
}
