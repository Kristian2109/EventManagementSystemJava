package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_ticket_template_order", columnNames = {"user", "ticket_template"})
})

@NamedEntityGraph(name = "BoughtTicket.relations", attributeNodes = {
        @NamedAttributeNode(
                value = "ticketTemplate",
                subgraph = "TicketTemplate.relations")
}, subgraphs = {
        @NamedSubgraph(name = "TicketTemplate.relations", attributeNodes = {
                @NamedAttributeNode(value = "event")
        })
})
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

    public String getTicketImage() {
        return ticketTemplate.getEvent().getImageFile().getEncoded();
    }
}
