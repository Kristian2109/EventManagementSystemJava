package com.projects.eventsbook.service.ticketCard;

import com.projects.eventsbook.DAO.*;
import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.TicketCard;
import com.projects.eventsbook.entity.TicketTemplate;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CardServiceImpl implements CardService {

    private final CardRepositoryJPA cardRepositoryJPA;
    private final UserRepository userRepository;
    private final BoughtTicketRepositoryJPA boughtTicketRepositoryJPA;
    private final TicketTemplateRepositoryJPA ticketTemplateRepositoryJPA;
    private final UserService userService;
    private final EventRepositoryJPA eventRepositoryJPA;

    @Autowired
    public CardServiceImpl(CardRepositoryJPA cardRepositoryJPA, UserRepository userRepository,
                           BoughtTicketRepositoryJPA boughtTicketRepositoryJPA,
                           TicketTemplateRepositoryJPA ticketTemplateRepositoryJPA, UserService userService, EventRepositoryJPA eventRepositoryJPA) {
        this.cardRepositoryJPA = cardRepositoryJPA;
        this.userRepository = userRepository;
        this.boughtTicketRepositoryJPA = boughtTicketRepositoryJPA;
        this.ticketTemplateRepositoryJPA = ticketTemplateRepositoryJPA;
        this.userService = userService;
        this.eventRepositoryJPA = eventRepositoryJPA;
    }

    @Override
    public void addTicketToCard(Long userId, Long ticketTemplateId) {
        User user = userService.getById(userId);
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
        userRepository.save(user);
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
    public BoughtTicket checkoutCard(Long ticketCardId) {

        TicketCard ticketCardToCheckout = getById(ticketCardId);
        Integer ticketsToBuy = ticketCardToCheckout.getTicketsCount();
        TicketTemplate ticketTemplateToBuy = ticketCardToCheckout.getTicketTemplate();
        Integer currentTicketsCount = ticketTemplateToBuy.getCurrentTicketsCount();

        if (ticketsToBuy > currentTicketsCount) {
            throw new InvalidOperationException("You can buy only " + currentTicketsCount + " tickets.");
        }
        ticketTemplateToBuy.setCurrentTicketsCount(ticketTemplateToBuy.getCurrentTicketsCount() - ticketsToBuy);
        BoughtTicket boughtTickets = new BoughtTicket(
                ticketTemplateToBuy,
                ticketCardToCheckout.getUser(),
                ticketsToBuy
        );
        cardRepositoryJPA.delete(ticketCardToCheckout);
        ticketTemplateRepositoryJPA.save(ticketTemplateToBuy);
        return boughtTicketRepositoryJPA.save(boughtTickets);
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
        User currentUser = userService.getById(userId);
        return currentUser.getTicketCards();
    }

    private void createCard(User user, TicketTemplate ticketTemplate) {
        List<TicketCard> userCards = user.getTicketCards();
        userCards.add(new TicketCard(user, ticketTemplate, 1));
        userRepository.save(user);
    }


    private Optional<TicketCard> getTicketCardIfExisting(User user, TicketTemplate ticketTemplate) {
        return user
                .getTicketCards()
                .stream()
                .filter(eventTicketCard ->
                        eventTicketCard.getTicketTemplate().getId().equals(ticketTemplate.getId())).findFirst();
    }
}
