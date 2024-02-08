package com.projects.eventsbook.service.ticketCard;

import com.projects.eventsbook.DAO.*;
import com.projects.eventsbook.entity.TicketCard;
import com.projects.eventsbook.entity.TicketTemplate;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.service.order.OrderService;
import com.projects.eventsbook.service.user.UserService;
import com.projects.eventsbook.util.RetrieveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardManagerImpl implements CardManager{
    private final CardRepositoryJPA cardRepositoryJPA;
    private final UserRepository userRepository;
    private final EventRepositoryJPA eventRepositoryJPA;
    private final OrderService orderService;

    @Autowired
    public CardManagerImpl(CardRepositoryJPA cardRepositoryJPA, UserRepository userRepository,
                           BoughtTicketRepositoryJPA boughtTicketRepositoryJPA,
                           TicketTemplateRepositoryJPA ticketTemplateRepositoryJPA, UserService userService, EventRepositoryJPA eventRepositoryJPA, OrderService orderService) {
        this.cardRepositoryJPA = cardRepositoryJPA;
        this.userRepository = userRepository;
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.orderService = orderService;
    }

    @Override
    public void addTicketToCard(Long userId, Long ticketTemplateId) {
        User user = RetrieveUtil.getByIdWithException(this.userRepository, userId);
        Optional<TicketTemplate> ticketTemplate = eventRepositoryJPA.findTicketTemplateById(ticketTemplateId);
        if (ticketTemplate.isEmpty()) {
            throw new NoEntityFoundException("No such ticket template");
        }
        Optional<TicketCard> currentTicketCard = getTicketCardIfExisting(user, ticketTemplate.get());
        if (currentTicketCard.isEmpty()) {
            createCard(user, ticketTemplate.get());
            return;
        }
        currentTicketCard.get().incrementTicketsCount();
        cardRepositoryJPA.save(currentTicketCard.get());
    }

    @Override
    public void removeTicketFromCard(Long ticketCardId) {
        TicketCard ticketCard = getById(ticketCardId);
        if (ticketCard.getTicketsCount() == 1) {
            cardRepositoryJPA.delete(ticketCard);
            return;
        }
        ticketCard.decrementTicketsCount();
        cardRepositoryJPA.save(ticketCard);
    }

    @Override
    public void createOrderFromCard(Long ticketTemplateId, Long userId) {
        User currentUser = RetrieveUtil.getByIdWithException(this.userRepository, userId);
        Optional<TicketCard> card = currentUser.getTicketCardByTicket(ticketTemplateId);
        if (card.isEmpty()) {
            throw new NoEntityFoundException("No such card");
        }
        Long eventId = card.get().getTicketTemplate().getEvent().getId();
        cardRepositoryJPA.delete(card.get());
        orderService.createOrder(currentUser.getId(), eventId, card.get().getTicketTemplate().getId(), card.get().getTicketsCount());
    }

    @Transactional(readOnly = true)
    @Override
    public TicketCard getById(Long id) {
        Optional<TicketCard> ticketCard = cardRepositoryJPA.findById(id);
        if (ticketCard.isEmpty()) {
            throw new InvalidOperationException("Not Ticket Card found");
        }
        return ticketCard.get();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketCard> getCardsByUserId(Long userId) {
        User currentUser = RetrieveUtil.getByIdWithException(this.userRepository, userId);
        return currentUser.getTicketCards();
    }

    private void createCard(User user, TicketTemplate ticketTemplate) {
        cardRepositoryJPA.save(new TicketCard(user, ticketTemplate, 1));
    }


    private Optional<TicketCard> getTicketCardIfExisting(User user, TicketTemplate ticketTemplate) {
        return user
                .getTicketCards()
                .stream()
                .filter(eventTicketCard ->
                        eventTicketCard.getTicketTemplate().getId().equals(ticketTemplate.getId())).findFirst();
    }
}
