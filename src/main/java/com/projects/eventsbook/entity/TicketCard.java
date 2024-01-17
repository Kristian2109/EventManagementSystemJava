package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @ManyToOne
    private User user;
    @NotNull
    @ManyToOne
    private TicketTemplate ticketTemplate;
    @NotNull
    private Integer ticketsCount;

    public TicketCard(User user, TicketTemplate ticketTemplate, Integer ticketsCount) {
        this.user = user;
        this.ticketTemplate = ticketTemplate;
        this.ticketsCount = ticketsCount;
    }

    public void incrementTicketsCount() {
        this.ticketsCount += 1;
    }

    public void decrementTicketsCount() {
        this.ticketsCount -= 1;
    }
}
