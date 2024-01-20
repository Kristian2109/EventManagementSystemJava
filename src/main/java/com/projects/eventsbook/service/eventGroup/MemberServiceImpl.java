package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DAO.GroupRepositoryJpa;
import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.enumerations.GroupMemberStatus;
import com.projects.eventsbook.enumerations.GroupRole;
import com.projects.eventsbook.exceptions.DuplicateResourceException;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.service.event.AuthorizationService;
import com.projects.eventsbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final EventGroupService eventGroupService;
    private final GroupRepositoryJpa groupRepositoryJpa;
    private final UserService userService;
    private final AuthorizationService authorizationService;

    @Autowired
    public MemberServiceImpl(EventGroupService eventGroupService, GroupRepositoryJpa groupRepositoryJpa, UserService userService, AuthorizationService authorizationService) {
        this.eventGroupService = eventGroupService;
        this.groupRepositoryJpa = groupRepositoryJpa;
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @Override
    public GroupMember getMemberFromGroup(EventGroup eventGroup, Long userId) {
        Optional<GroupMember> foundMember = eventGroup.getMembers().stream().filter((groupMember -> {
            return groupMember.getUser().getId().equals(userId);
        })).findFirst();

        if (foundMember.isEmpty()) {
            throw new NoEntityFoundException("No Such Member in this group!");
        }
        return foundMember.get();
    }

    @Override
    public void invite(CreateMemberDTO createMemberDTO, Long actorId) {
        EventGroup eventGroup = eventGroupService.get(createMemberDTO.getGroupId());
        GroupMember actor = this.getMemberFromGroup(eventGroup, actorId);
        authorizationService.validateManageMembershipRequest(actor);

        createInvitationOrRequest(createMemberDTO.getUserId(), eventGroup, true);
    }

    @Override
    public void request(CreateMemberDTO createMemberDTO, Long actorId) {
        EventGroup eventGroup = eventGroupService.get(createMemberDTO.getGroupId());
        if  (!actorId.equals(createMemberDTO.getUserId())) {
            throw new InvalidOperationException("Only current user can make requests to a group!");
        }
        createInvitationOrRequest(createMemberDTO.getUserId(), eventGroup, false);
    }

    private void createInvitationOrRequest(Long toBeMadeMemberId, EventGroup eventGroup, boolean isInvitation) {
        User toMakeMember = userService.getById(toBeMadeMemberId);
        Optional<GroupMember> groupMember = groupRepositoryJpa.findMemberByGroupAndUserId(eventGroup.getId(),  toBeMadeMemberId);

        if (groupMember.isPresent()) {
            throw new DuplicateResourceException("User has interacted with the group.");
        }
        createMember(eventGroup,  toMakeMember, isInvitation);
    }

    private void createMember(EventGroup eventGroup, User toMakeMember, boolean isInvitation) {
        GroupMember newGroupMember = new GroupMember(
                toMakeMember,
                eventGroup,
                GroupRole.VIEWER,
                isInvitation
        );
        newGroupMember.setStatus(GroupMemberStatus.PENDING);
        eventGroup.addMember(newGroupMember);
        groupRepositoryJpa.save(eventGroup);
    }


    @Override
    public void acceptInvitation(Long userId, Long groupId, Long actorId) {
        decideInvitation(userId, groupId, actorId, GroupMemberStatus.ACCEPTED);
    }

    @Override
    public void rejectInvitation(Long userId, Long groupId, Long actorId) {
        decideInvitation(userId, groupId, actorId, GroupMemberStatus.REJECTED);
    }

    private void decideInvitation(Long userId, Long groupId, Long actorId, GroupMemberStatus status) {
        EventGroup eventGroup = eventGroupService.get(groupId);
        if (!actorId.equals(userId)) {
            throw new InvalidOperationException("Only current user can decide the invitation!");
        }

        GroupMember member = this.getMemberFromGroup(eventGroup, userId);

        if (!member.isUndecidedInvitation()) {
            throw new InvalidOperationException("Invalid operation");
        }
        member.setDecidedAt(LocalDateTime.now());
        member.setStatus(status);
        groupRepositoryJpa.save(eventGroup);
    }

    @Override
    public void rejectRequest(Long toBeRejectedId, Long actorId, Long groupId) {
        decideRequest(toBeRejectedId, groupId,actorId, GroupMemberStatus.REJECTED);
    }

    @Override
    public void acceptRequest(Long toBeAcceptedId, Long actorId, Long groupId) {
        decideRequest(toBeAcceptedId, groupId ,actorId, GroupMemberStatus.ACCEPTED);
    }


    private void decideRequest(Long groupId, Long userId, Long actorId, GroupMemberStatus groupMemberStatus) {
        EventGroup eventGroup = eventGroupService.get(groupId);
        GroupMember actor = this.getMemberFromGroup(eventGroup, actorId);
        authorizationService.validateManageMembershipRequest(actor);

        GroupMember toBeMadeMember = this.getMemberFromGroup(eventGroup, userId);

        if (!toBeMadeMember.isUndecidedRequest()) {
            throw new InvalidOperationException("Membership is already decided.");
        }


        toBeMadeMember.setStatus(groupMemberStatus);
        toBeMadeMember.setDecidedAt(LocalDateTime.now());
        groupRepositoryJpa.save(eventGroup);
    }

    @Override
    public void removeMember(Long toBeRemovedId, Long groupId) {
        EventGroup group = eventGroupService.get(groupId);
        List<GroupMember> members = group.getMembers();
        GroupMember toBeRemoved = this.getMemberFromGroup(group, toBeRemovedId);
        members.remove(toBeRemoved);
        groupRepositoryJpa.save(group);
    }

    @Override
    public void changeGroupMemberRole(Long groupId, Long toBeChangedId, Long actorId, GroupRole groupRole) {
        EventGroup group = eventGroupService.get(groupId);
        GroupMember actor = this.getMemberFromGroup(group, actorId);
        GroupMember toBeChanged = this.getMemberFromGroup(group, toBeChangedId);

        authorizationService.validateChangeGroupRole(actor, toBeChanged, groupRole);

        toBeChanged.setGroupRole(groupRole);
        groupRepositoryJpa.save(group);
    }


    @Transactional(readOnly = true)
    @Override
    public GroupMember getGroupMemberByGroupAndUser(Long userId, Long groupId) {
        Optional<GroupMember> groupMember = groupRepositoryJpa.findMemberByGroupAndUserId(userId, groupId);
        if (groupMember.isEmpty()) {
            throw new NoEntityFoundException("No membership invitation found.");
        }
        return groupMember.get();
    }
}
