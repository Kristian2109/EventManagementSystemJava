package com.projects.eventsbook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TicketActivation extends IdentityClassBase {
    private String activationToken;
    private LocalDateTime activationDate;
    @ManyToOne(targetEntity = BoughtTicket.class)
    private BoughtTicket order;

    public TicketActivation(String activationToken, BoughtTicket order) {
        this.activationToken = activationToken;
        this.order = order;
    }

    public String toString() {
        return "Token: " + activationToken + '\n';
    }

}
