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
public class TicketTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @ManyToOne
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
