package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.enumerations.GroupRole;

public interface MemberService {

    public GroupMember getMemberByGroupIdAndUserId(Long userId, Long groupId);
    public GroupMember invite(CreateMemberDTO createMemberDTO);
    public GroupMember acceptInvitation(Long groupMemberId);
    public GroupMember rejectInvitation(Long groupMemberId);
    public GroupMember request(CreateMemberDTO createMemberDTO);
    public GroupMember rejectRequest(Long groupMemberId);
    public GroupMember acceptRequest(Long groupMemberId);
    public GroupMember removeMember(Long groupMemberId);
    public GroupMember modifyMember(Long groupMemberId, GroupRole groupRole);
    public GroupMember getGroupMemberById(Long id);
    public GroupMember getGroupMemberByGroupAndUser(Long userId, Long groupId);
}
