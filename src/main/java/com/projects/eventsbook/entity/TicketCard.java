package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TicketCard extends IdentityClassBase {
    @NotNull
    @ManyToOne(optional = false)
    private User user;
    @NotNull
    @ManyToOne(optional = false)
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
