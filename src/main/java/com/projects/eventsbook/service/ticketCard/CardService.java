package com.projects.eventsbook.service.ticketCard;
import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.TicketCard;

import java.util.List;

public interface CardService {
    public void addTicketToCard(Long userId, Long ticketTemplateId);
    public void removeTicketFromCard(Long ticketCardId);
    public BoughtTicket checkoutCard(Long ticketCardId);
    public TicketCard getById(Long id);
    List<TicketCard> getCardsByUserId(Long userId);
}