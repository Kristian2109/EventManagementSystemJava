package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.enumerations.GroupRole;

public interface MemberService {

    public GroupMember getMemberFromGroup(EventGroup eventGroup, Long userId);
    void invite(CreateMemberDTO createMemberDTO, Long actorId);
    public void acceptInvitation(Long userId, Long groupId, Long actorId);
    void rejectInvitation(Long userId, Long groupId, Long actorId);
    void request(CreateMemberDTO createMemberDTO, Long actorId);
    public void rejectRequest(Long toBeAcceptedId, Long actorId, Long groupId);
    public void acceptRequest(Long toBeAcceptedId, Long actorId, Long groupId);
    public void removeMember(Long userId, Long groupId);
    public void changeGroupMemberRole(Long groupId, Long toBeChangedId, Long actorId, GroupRole groupRole);
    public GroupMember getGroupMemberByGroupAndUser(Long userId, Long groupId);
}
