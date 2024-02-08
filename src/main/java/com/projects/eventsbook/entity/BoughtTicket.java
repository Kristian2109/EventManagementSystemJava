package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_ticket_template_order", columnNames = {"user", "ticket_template"})
})

@NamedEntityGraph(name = "BoughtTicket.relations", attributeNodes = {
        @NamedAttributeNode(
                value = "ticketActivations"
        ),
        @NamedAttributeNode(
                value = "ticketTemplate",
                subgraph = "TicketTemplate.relations")
}, subgraphs = {
        @NamedSubgraph(name = "TicketTemplate.relations", attributeNodes = {
                @NamedAttributeNode(value = "event", subgraph = "Event.image")
        }),
        @NamedSubgraph(name = "Event.image", attributeNodes = {
                @NamedAttributeNode(value = "imageFile")
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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<TicketActivation> ticketActivations = new ArrayList<>();

    public BoughtTicket(TicketTemplate ticketTemplate, User boughtBy, Integer ticketsCount) {
        this.ticketTemplate = ticketTemplate;
        this.boughtBy = boughtBy;
        this.ticketsCount = ticketsCount;
    }

    public String getTicketImage() {
        return ticketTemplate.getEvent().getImageFile().getEncoded();
    }

    @Override
    public String toString() {
        return "BoughtTicket{" +
                "ticketTemplate=" + ticketTemplate +
                ", ticketsCount=" + ticketsCount +
                '}';
    }
}
