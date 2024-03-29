package com.projects.eventsbook.web;

import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.mapper.UserMapper;
import com.projects.eventsbook.service.order.TicketManager;
import com.projects.eventsbook.service.user.BalanceManager;
import com.projects.eventsbook.service.utils.QrCodeGenerator;
import com.projects.eventsbook.service.eventGroup.EventGroupService;
import com.projects.eventsbook.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class UserController {

    private final UserService userService;
    private final EventGroupService eventGroupService;
    private final TicketManager ticketManager;
    private final BalanceManager balanceManager;

    @Autowired
    public UserController(UserService userService, EventGroupService eventGroupService, QrCodeGenerator qrCodeGenerator, TicketManager ticketManager, BalanceManager balanceManager) {
        this.userService = userService;
        this.eventGroupService = eventGroupService;
        this.ticketManager = ticketManager;
        this.balanceManager = balanceManager;
    }

    @GetMapping
    public String personalInfo(Model model,
                               UserProfileDTO currentUser) {
        User userFromDB = userService.getById(currentUser.getId());
        currentUser = UserMapper.toUserProfileDTO(userFromDB);
        model.addAttribute("userProfileDTO", currentUser);
        return "profile";
    }

    @GetMapping("/groups")
    public String groups(Model model, UserProfileDTO currentUser) {
        List<EventGroup> eventGroups = eventGroupService.getUserGroups(currentUser.getId());
        model.addAttribute("groups", eventGroups);
        return "user-groups";
    }

    @GetMapping("/update")
    private @NonNull String updatePersonalInfo(Model model, UserProfileDTO currentUser) {
        model.addAttribute("userProfileDTO", currentUser);
        return "update-user";
    }

    @PostMapping("/update")
    private String updatePersonalInfo(@ModelAttribute("userProfileDTO") UserProfileDTO userProfileDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
            return "redirect:update";
        }
        try {
            User updatedUser = userService.update(UserMapper.toUser(userProfileDTO));
            httpSession.setAttribute("currentUser", UserMapper.toUserProfileDTO(updatedUser));
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:update";
        }

        return "redirect:/profile";
    }

    @GetMapping("/orders")
    public String renderBoughtTickets(Model model, UserProfileDTO loggedUser) {
        List<BoughtTicket> tickets = userService.getUserTickets(loggedUser.getId());
        model.addAttribute("tickets", tickets);
        return "boughtTickets";
    }

    @GetMapping("/tickets")
    public String renderTickets(Model model, UserProfileDTO loggedUser) {
        List<BoughtTicket> tickets = ticketManager.getUpcomingUserTickets(loggedUser.getId());
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @PostMapping("/balance/add")
    public String addBalance(UserProfileDTO loggedUser,
                             @RequestParam("currentBalance") Double currentBalance,
                             @RequestParam("amount") Double amountToAdd,
                             RedirectAttributes redirectAttributes) {
        try {
            balanceManager.addMoneyToUser(loggedUser.getId(), currentBalance, amountToAdd);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/balance/draw")
    public String drawMoneyFromAccount(UserProfileDTO loggedUser,
                                       @RequestParam("currentBalance") Double currentBalance,
                                       @RequestParam("amount") Double amountToDraw,
                                       RedirectAttributes redirectAttributes) {
        try {
            balanceManager.drawMoneyFromUser(loggedUser.getId(), currentBalance, amountToDraw);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/profile";
    }

    @GetMapping("/error")
    public String renderError() {
        return "error";
    }
}
