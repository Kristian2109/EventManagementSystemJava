package com.projects.eventsbook.web;

import com.google.zxing.qrcode.decoder.Mode;
import com.projects.eventsbook.DTO.eventDomain.CreateEventDTO;
import com.projects.eventsbook.DTO.eventDomain.CreateReviewDTO;
import com.projects.eventsbook.DTO.eventDomain.CreateTicketTemplateDTO;
import com.projects.eventsbook.DTO.eventDomain.EventDetails;
import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.ImageFile;
import com.projects.eventsbook.entity.Review;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.service.event.AuthorizationService;
import com.projects.eventsbook.service.order.TicketManager;
import com.projects.eventsbook.service.utils.FileService;
import com.projects.eventsbook.service.event.EventService;
import com.projects.eventsbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final FileService fileService;
    private final TicketManager ticketManager;
    private final AuthorizationService authorizationService;


    @Autowired
    public EventController(EventService eventService, UserService userService, FileService fileService, TicketManager ticketManager, AuthorizationService authorizationService) {
        this.eventService = eventService;
        this.userService = userService;
        this.fileService = fileService;
        this.ticketManager = ticketManager;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/create")
    public String renderCreate(Model model,
                               UserProfileDTO userProfileDTO,
                               RedirectAttributes redirectAttributes,
                               @RequestParam("groupId") Long groupId) {
        try {
            CreateEventDTO toCreate = new CreateEventDTO();
            toCreate.setCreatedById(userProfileDTO.getId());
            toCreate.setEventGroupId(groupId);
            model.addAttribute("createEventDTO", toCreate);
            return "create-event";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/profile/personalInfo";
        }
    }

    @PostMapping
    public String createEvent(@ModelAttribute("createEventDTO") CreateEventDTO createEventDTO,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("error", bindingResult.getAllErrors());
            return "redirect:/events/create";
        }
        try {
            ImageFile createdFile = fileService.storeFile(file);
            createEventDTO.setImageDataId(createdFile.getId());
            Event createdEvent = eventService.create(createEventDTO);
            return "redirect:/events/" + createdEvent.getId();
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/groups/" + createEventDTO.getEventGroupId();
        }
    }

    @GetMapping("/{eventId}")
    public String renderEvent(@PathVariable Long eventId,
                              Model model,
                              UserProfileDTO userProfileDTO,
                              RedirectAttributes redirectAttributes,
                              @RequestParam(name = "pageNumberReviews", defaultValue = "1") int pageNumber,
                              @RequestParam(name = "pageSizeReviews", defaultValue = "10") int pageSize
                              ) {
        Event event = eventService.getById(eventId);
        List<Review> reviews = event.getByPage(pageNumber, pageSize);
        if (event.getImageFile() != null) {
            model.addAttribute("image", fileService.encodeImage(event.getImageFile()));
        }
        User user = userService.getById(userProfileDTO.getId());
        if (!event.canUserAccessEvent(user)) {
            redirectAttributes.addFlashAttribute("error", "User don't have access to the event.");
            return "redirect:/profile";
        }
        model.addAttribute(event);
        model.addAttribute(reviews);
        model.addAttribute("canUserMakeReview", authorizationService.canUserMakeReviewToEvent(user, event));
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("isAdmin", event.canUserModifyEvent(user));
        return "event";
    }

    @GetMapping("/{eventId}/createTicket")
    public String renderAddTicket(Model model, @PathVariable("eventId") Long eventId) {
        CreateTicketTemplateDTO ticketTemplateDTO = new CreateTicketTemplateDTO();
        ticketTemplateDTO.setEventId(eventId);
        model.addAttribute("createTicketDTO", ticketTemplateDTO);
        return "create-ticket";
    }

    @PostMapping("/createTicket")
    public String createTicket(@ModelAttribute("createTicketDTO") CreateTicketTemplateDTO createTicketTemplateDTO,
                               RedirectAttributes redirectAttributes) {
        try {
            eventService.addTicket(createTicketTemplateDTO);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/events/" + createTicketTemplateDTO.getEventId();
    }

    @GetMapping("/{eventId}/createReview")
    public String renderCreateReview(Model model,
                                     @PathVariable("eventId") Long eventId) {
        model.addAttribute(new CreateReviewDTO());
        model.addAttribute(eventId);
        return "create-review";
    }

    @PostMapping("/{eventId}/reviews")
    public String createReview(@PathVariable("eventId") Long eventId,
                               UserProfileDTO currentUser,
                               @ModelAttribute("createReviewDTO") CreateReviewDTO createReviewDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("error", bindingResult.getAllErrors());
            return "redirect:/events/" + eventId;
        }

        try {
            createReviewDTO.setReviewerId(currentUser.getId());
            createReviewDTO.setEventId(eventId);
            eventService.makeReviewToEvent(createReviewDTO);
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }

        return "redirect:/events/" + eventId;
    }

    @PostMapping("/{eventId}/tickets/activate")
    public String activateTicket(@PathVariable("eventId") Long eventId,
                                 @RequestParam Long ticketId,
                                 UserProfileDTO userProfileDTO,
                                 RedirectAttributes redirectAttributes
                                 ) {
        try {
            String message = ticketManager.activateTicketByUser(eventId, ticketId, userProfileDTO.getId());
            redirectAttributes.addFlashAttribute("message", message);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events/" + eventId;
    }

    @GetMapping("/{eventId}/extended")
    public String getAdminEvent(@PathVariable Long eventId, Model model, UserProfileDTO userProfileDTO) {
        authorizationService.validateAccessEventAdminPanel(userProfileDTO.getId(), eventId);
        model.addAttribute("event", eventService.getEventDetailsForAdmin(eventId));
        return "event-admin";
    }
}
