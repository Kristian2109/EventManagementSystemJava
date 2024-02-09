package com.projects.eventsbook.service.order;

import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.TicketActivation;

import java.util.List;

public interface TicketManager {
    List<BoughtTicket> getUpcomingUserTickets(Long userId);
    void activateTicket(Long ticketId);
    void activateTicketByUser(Long eventId, Long ticketId, Long userId);
}
