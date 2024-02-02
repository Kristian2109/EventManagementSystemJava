package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.enumerations.GroupRole;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberService {

    @Transactional(readOnly = true)
    GroupMember getMemberFromGroup(EventGroup eventGroup, Long userId);
    void invite(CreateMemberDTO createMemberDTO, Long actorId);
    void acceptInvitation(Long userId, Long groupId, Long actorId);
    void rejectInvitation(Long userId, Long groupId, Long actorId);
    void request(CreateMemberDTO createMemberDTO, Long actorId);
    void rejectRequest(Long toBeAcceptedId, Long actorId, Long groupId);
    void acceptRequest(Long toBeAcceptedId, Long actorId, Long groupId);
    void removeMember(Long userId, Long groupId);
    void changeGroupMemberRole(Long groupId, Long toBeChangedId, Long actorId, GroupRole groupRole);
    @Transactional(readOnly = true)
    public GroupMember getGroupMemberByGroupAndUser(Long userId, Long groupId);
}
