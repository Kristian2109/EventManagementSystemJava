package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BoughtTicket extends IdentityClassBase {
    @ManyToOne(optional = false)
    @NotNull
    private TicketTemplate ticketTemplate;
    @ManyToOne(optional = false)
    @NotNull
    private User boughtBy;
    @NotNull
    private Integer ticketsCount;

    public BoughtTicket(TicketTemplate ticketTemplate, User boughtBy, Integer ticketsCount) {
        this.ticketTemplate = ticketTemplate;
        this.boughtBy = boughtBy;
        this.ticketsCount = ticketsCount;
    }
}
