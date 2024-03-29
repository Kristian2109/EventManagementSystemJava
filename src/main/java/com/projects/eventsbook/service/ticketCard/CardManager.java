package com.projects.eventsbook.service.ticketCard;

import com.projects.eventsbook.entity.TicketCard;

import java.util.List;

public interface CardManager {
    public void addTicketToCard(Long userId, Long ticketTemplateId);
    public void removeTicketFromCard(Long ticketCardId);

    void createOrderFromCard(Long ticketTemplateId, Long userId);

    public TicketCard getById(Long id);
    List<TicketCard> getCardsByUserId(Long userId);
}
