package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DAO.GroupRepositoryJpa;
import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.enumerations.GroupRole;
import com.projects.eventsbook.exceptions.NoEntityFoundException;

import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    private final EventGroupService eventGroupService;
    private final GroupRepositoryJpa groupRepositoryJpa;

    public MemberServiceImpl(EventGroupService eventGroupService, GroupRepositoryJpa groupRepositoryJpa) {
        this.eventGroupService = eventGroupService;
        this.groupRepositoryJpa = groupRepositoryJpa;
    }

    @Override
    public GroupMember getMemberByGroupIdAndUserId(Long userId, Long groupId) {
        Optional<GroupMember> groupMember = groupRepositoryJpa.findMemberByGroupAndUserId(userId, groupId);
        if (groupMember.isEmpty()) {
            throw new NoEntityFoundException("No Such Member!");
        }
        return groupMember.get();
    }

    @Override
    public GroupMember invite(CreateMemberDTO createMemberDTO) {
        return null;
    }

    @Override
    public GroupMember acceptInvitation(Long groupMemberId) {
        return null;
    }

    @Override
    public GroupMember rejectInvitation(Long groupMemberId) {
        return null;
    }

    @Override
    public GroupMember request(CreateMemberDTO createMemberDTO) {
        return null;
    }

    @Override
    public GroupMember rejectRequest(Long groupMemberId) {
        return null;
    }

    @Override
    public GroupMember acceptRequest(Long groupMemberId) {
        return null;
    }

    @Override
    public GroupMember removeMember(Long groupMemberId) {
        return null;
    }

    @Override
    public GroupMember modifyMember(Long groupMemberId, GroupRole groupRole) {
        return null;
    }

    @Override
    public GroupMember getGroupMemberById(Long id) {
        return null;
    }

    @Override
    public GroupMember getGroupMemberByGroupAndUser(Long userId, Long groupId) {
        return null;
    }
}
