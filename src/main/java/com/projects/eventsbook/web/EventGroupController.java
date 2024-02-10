package com.projects.eventsbook.web;


import com.projects.eventsbook.DTO.groupDomain.CreateGroupDTO;
import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.service.utils.FileService;
import com.projects.eventsbook.service.eventGroup.EventGroupService;
import com.projects.eventsbook.service.eventGroup.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class EventGroupController {

    private final EventGroupService eventGroupService;
    private final FileService fileService;
    private final MemberService memberService;

    @Autowired
    public EventGroupController(EventGroupService eventGroupService, FileService fileService, MemberService memberService) {
        this.eventGroupService = eventGroupService;
        this.fileService = fileService;
        this.memberService = memberService;
    }

    @GetMapping("/join")
    public String getGroupsForJoining(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                                      UserProfileDTO currentUser,
                                      Model model) {
        List<EventGroup> eventGroups = eventGroupService.getGroupsForUserToJoin(currentUser.getId());
        model.addAttribute("groups", eventGroups);
        model.addAttribute("userId", currentUser.getId());
        return "group-catalog";
    }

    @GetMapping("/{groupId}/members/request")
    public String sendGroupMembershipRequest(@PathVariable("groupId") Long groupId,
                                             UserProfileDTO currentUser) {;
        CreateMemberDTO createMemberDTO = new CreateMemberDTO(
                currentUser.getId(),
                groupId,
                "VIEWER"
        );
        memberService.request(createMemberDTO, currentUser.getId());
        return "redirect:/groups/join";
    }

    @GetMapping("/{groupId}/members/{userId}/accept-request")
    public String acceptGroupMembershipRequest(@PathVariable("groupId") Long groupId,
                                               @PathVariable("userId") Long userId,
                                               UserProfileDTO currentUser) {
        memberService.acceptRequest(userId, currentUser.getId(), groupId);
        return "redirect:/groups/" + groupId;
    }

    @GetMapping("/{groupId}")
    public String renderGroup(@PathVariable("groupId") Long groupId,
                              UserProfileDTO currentUser,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            EventGroup foundEventGroup = eventGroupService.get(groupId);
            model.addAttribute("group", foundEventGroup);
            GroupMember groupMember;
            if (currentUser != null) {
                groupMember = memberService.getGroupMemberByGroupAndUser(currentUser.getId(), foundEventGroup.getId());
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
                               UserProfileDTO currentUser,
                               RedirectAttributes redirectAttributes) {
        CreateGroupDTO toCreate= new CreateGroupDTO();
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
