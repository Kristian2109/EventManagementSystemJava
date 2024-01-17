package com.projects.eventsbook.web;


import com.projects.eventsbook.DTO.groupDomain.CreateGroupDTO;
import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.ImageFile;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.service.FileService;
import com.projects.eventsbook.service.eventGroup.EventGroupService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class EventGroupController {

    private final EventGroupService eventGroupService;
    private final FileService fileService;

    @Autowired
    public EventGroupController(EventGroupService eventGroupService, FileService fileService) {
        this.eventGroupService = eventGroupService;
        this.fileService = fileService;
    }

    @GetMapping("/join")
    public String getGroupsForJoining(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                                      HttpSession session,
                                      Model model) {

        UserProfileDTO userProfileDTO = (UserProfileDTO) session.getAttribute("currentUser");
        List<EventGroup> eventGroups = eventGroupService.getGroupsForUserToJoin(userProfileDTO.getId());
        model.addAttribute("groups", eventGroups);
        model.addAttribute("userId", userProfileDTO.getId());
        return "group-catalog";
    }

    @GetMapping("/{groupId}/members/{userId}/request")
    public String sendGroupMembershipRequest(@PathVariable("groupId") Long groupId,
                                           @PathVariable("userId") Long userId) {
        CreateMemberDTO createMemberDTO = new CreateMemberDTO(
                userId,
                groupId,
                "VIEWER"
        );
        eventGroupService.request(createMemberDTO);
        return "redirect:/groups/join";
    }

    @GetMapping("/{groupId}/members/{groupMemberId}/accept-request")
    public String acceptGroupMembershipRequest(@PathVariable("groupId") Long groupId,
                                             @PathVariable("groupMemberId") Long groupMemberId) {
        eventGroupService.acceptRequest(groupMemberId);
        return "redirect:/groups/" + groupId;
    }

    @GetMapping("/{groupId}")
    public String renderGroup(@PathVariable("groupId") Long groupId,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            EventGroup foundEventGroup = eventGroupService.get(groupId);
            model.addAttribute("group", foundEventGroup);
            GroupMember groupMember;
            if (session.getAttribute("currentUser") != null) {
                UserProfileDTO currentUser = (UserProfileDTO) session.getAttribute("currentUser");
                groupMember = eventGroupService.getGroupMemberByGroupAndUser(currentUser.getId(), foundEventGroup.getId());
                model.addAttribute(groupMember);
            }
            if (foundEventGroup.getImageFile() != null) {
                model.addAttribute("image", fileService.encodeImage(foundEventGroup.getImageFile()));
            }
            return "group";
        } catch (NoEntityFoundException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/groups";
        }
    }

    @GetMapping("/create")
    public String renderCreate(Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        CreateGroupDTO toCreate= new CreateGroupDTO();
        UserProfileDTO currentUser = (UserProfileDTO) session.getAttribute("currentUser");
        if (currentUser.getIdentityNumber() == null || currentUser.getIdentityNumber().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User should have identity number!");
            return "redirect:/profile";
        }
        toCreate.setOwnerId(currentUser.getId());
        model.addAttribute("createGroupDTO", toCreate);
        return "create-group";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("createGroupDTO") CreateGroupDTO createGroupDTO,
                         RedirectAttributes redirectAttributes) {
        try {
            EventGroup created = eventGroupService.create(createGroupDTO);
            return "redirect:/groups/" + created.getId();
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:create";
        }
    }
}
