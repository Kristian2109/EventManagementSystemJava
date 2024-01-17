package com.projects.eventsbook.web;

import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.mapper.UserMapper;
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

    @Autowired
    public UserController(UserService userService, EventGroupService eventGroupService) {
        this.userService = userService;
        this.eventGroupService = eventGroupService;
    }

    @GetMapping
    public String personalInfo(Model model, HttpSession session) {
        UserProfileDTO currentUser = (UserProfileDTO) session.getAttribute("currentUser");
        model.addAttribute("userProfileDTO", currentUser);
        return "profile";
    }

    @GetMapping("/groups")
    public String groups(Model model, HttpSession session) {
        UserProfileDTO currentUser = (UserProfileDTO) session.getAttribute("currentUser");
        List<EventGroup> eventGroups = eventGroupService.getUserGroups(currentUser.getId());
        model.addAttribute("groups", eventGroups);
        return "user-groups";
    }

    @GetMapping("/update")
    private @NonNull String updatePersonalInfo(Model model, HttpSession session) {
        UserProfileDTO currentUser = (UserProfileDTO) session.getAttribute("currentUser");
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

        return "redirect:profile";
    }

}
