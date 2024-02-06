package com.projects.eventsbook.web;

import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.entity.TicketCard;
import com.projects.eventsbook.service.ticketCard.CardManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/card")
public class CardController {
    private final CardManager cardManager;

    @Autowired
    public CardController(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    @GetMapping
    public String getCardsForUser(HttpSession httpSession,
                                  Model model) {
        UserProfileDTO currentUser = (UserProfileDTO) httpSession.getAttribute("currentUser");
        List<TicketCard> userCards = cardManager.getCardsByUserId(currentUser.getId());
        model.addAttribute(userCards);
        return "card";
    }

    @GetMapping("/tickets/{ticketTemplateId}")
    public String addTicketToCard(HttpSession session,
                                  @PathVariable Long ticketTemplateId,
                                  @RequestParam("eventId") Long eventId) {
        UserProfileDTO currentUser = (UserProfileDTO) session.getAttribute("currentUser");
        Long userId = currentUser.getId();
        cardManager.addTicketToCard(userId, ticketTemplateId);
        return "redirect:/events/" + eventId;
    }

    @PostMapping("/ticket/{ticketTemplateId}")
    public String checkoutCard(@PathVariable Long ticketTemplateId, UserProfileDTO loggedUser) {
        cardManager.createOrderFromCard(ticketTemplateId, loggedUser.getId());
        return "redirect:/card";
    }

    @PostMapping("/{cardId}/remove")
    public String removeOneTicket(@PathVariable Long cardId) {
        cardManager.removeTicketFromCard(cardId);
        return "redirect:/card";
    }
}
