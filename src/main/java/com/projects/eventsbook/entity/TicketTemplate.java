package com.projects.eventsbook.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TicketTemplate extends IdentityClassBase{
    @NotNull
    @ManyToOne(optional = false)
    private Event event;
    @NotNull
    private Double price;
    @NotNull
    private Integer initialTicketsCount;
    @NotNull
    private String description;
    @NotNull
    private Integer currentTicketsCount;
}
