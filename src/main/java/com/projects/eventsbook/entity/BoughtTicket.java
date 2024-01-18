package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BoughtTicket extends IdentityClassBase{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @NotNull
    private TicketTemplate ticketTemplate;
    @ManyToOne
    @NotNull
    private User boughtBy;
    @NotNull
    private Integer ticketsCount;
    @NonNull
    private LocalDateTime boughtAt = LocalDateTime.now();

    public BoughtTicket(TicketTemplate ticketTemplate, User boughtBy, Integer ticketsCount) {
        this.ticketTemplate = ticketTemplate;
        this.boughtBy = boughtBy;
        this.ticketsCount = ticketsCount;
    }
}
