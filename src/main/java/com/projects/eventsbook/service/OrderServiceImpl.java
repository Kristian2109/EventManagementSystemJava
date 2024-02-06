package com.projects.eventsbook.service;

import com.projects.eventsbook.DAO.BoughtTicketRepositoryJPA;
import com.projects.eventsbook.DAO.EventRepositoryJPA;
import com.projects.eventsbook.DAO.GroupRepositoryJpa;
import com.projects.eventsbook.DAO.UserRepository;
import com.projects.eventsbook.entity.*;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.util.RetrieveUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Transactional
@Component
public class OrderServiceImpl implements OrderService{
    private final UserRepository userRepository;
    private final BoughtTicketRepositoryJPA boughtTicketRepository;
    private final EventRepositoryJPA eventRepository;
    private final GroupRepositoryJpa groupRepository;

    public OrderServiceImpl(UserRepository userRepository, BoughtTicketRepositoryJPA boughtTicketRepository, EventRepositoryJPA eventRepository, GroupRepositoryJpa groupRepository) {
        this.userRepository = userRepository;
        this.boughtTicketRepository = boughtTicketRepository;
        this.eventRepository = eventRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void createOrder(Long userId, Long eventId, Long ticketTemplateId, Integer ticketsCount) {
        User currentUser = RetrieveUtil.getByIdWithException(this.userRepository, userId);
        Event currentEvent = RetrieveUtil.getByIdWithException(this.eventRepository, eventId);

        Optional<TicketTemplate> ticketToBuy = currentEvent.getTicketTemplateById(ticketTemplateId);
        if (ticketToBuy.isEmpty()) {
            throw new NoEntityFoundException("No such ticket template!");
        }
        TicketTemplate ticket = ticketToBuy.get();


        double orderPrice = ticketsCount * ticket.getPrice();
        if (currentUser.getBalance() < orderPrice) {
            throw new InvalidOperationException("Insufficient funds in account");
        }
        if (ticket.getCurrentTicketsCount() < ticketsCount) {
            throw new InvalidOperationException("You can but only " + ticket.getCurrentTicketsCount() + " tickets");
        }
        currentUser.setBalance(currentUser.getBalance() - orderPrice);
        currentEvent.getEventGroup().setBalance(currentEvent.getEventGroup().getBalance() + orderPrice);
        ticket.setCurrentTicketsCount(ticket.getCurrentTicketsCount() - ticketsCount);
        BoughtTicket order = new BoughtTicket(ticket, currentUser, ticketsCount);
        eventRepository.save(currentEvent);
        userRepository.save(currentUser);
        createActivations(order);
        boughtTicketRepository.save(order);
        groupRepository.save(currentEvent.getEventGroup());
    }

    private void createActivations(BoughtTicket order) {
        IntStream.range(0, order.getTicketsCount())
                .forEach(number -> {
                    order
                            .getTicketActivations()
                            .add(new TicketActivation(UUID.randomUUID().toString(), order));
                });
    }
}
