package com.projects.eventsbook.service.eventGroup;

import com.projects.eventsbook.DAO.GroupRepositoryJpa;
import com.projects.eventsbook.DAO.GroupMemberRepositoryJpa;
import com.projects.eventsbook.DTO.groupDomain.CreateGroupDTO;
import com.projects.eventsbook.DTO.groupDomain.CreateMemberDTO;
import com.projects.eventsbook.DTO.groupDomain.UpdateGroupDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.ImageFile;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.enumerations.GroupMemberStatus;
import com.projects.eventsbook.enumerations.GroupRole;
import com.projects.eventsbook.exceptions.DuplicateResourceException;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.exceptions.InvalidUserDataException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.mapper.GroupMapper;
import com.projects.eventsbook.service.FileService;
import com.projects.eventsbook.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class EventGroupServiceImpl implements EventGroupService{

    private final GroupRepositoryJpa groupRepositoryJpa;
    private final EventGroupValidator eventGroupValidator;
    private final GroupMemberRepositoryJpa groupMemberRepositoryJpa;
    private final UserService userService;
    private final FileService fileService;

    public EventGroupServiceImpl(GroupRepositoryJpa groupRepositoryJpa, EventGroupValidator eventGroupValidator, GroupMemberRepositoryJpa groupMemberRepositoryJpa, UserService userService, FileService fileService) {
        this.groupRepositoryJpa = groupRepositoryJpa;
        this.eventGroupValidator = eventGroupValidator;
        this.groupMemberRepositoryJpa = groupMemberRepositoryJpa;
        this.userService = userService;
        this.fileService = fileService;
    }

    @Override
    public EventGroup create(CreateGroupDTO createGroupDTO) {
        eventGroupValidator.validateCreateGroup(createGroupDTO);
        EventGroup toCreate = GroupMapper.toGroup(createGroupDTO);
        User foundUser = userService.getById(createGroupDTO.getOwnerId());

        if (foundUser.getIdentityNumber() == null) {
            throw new InvalidUserDataException("User should have identity Number to create a group");
        }

        toCreate.setOwner(foundUser);
        GroupMember ownerMember = new GroupMember(
                foundUser,
                toCreate,
                GroupRole.ADMIN,
                false,
                LocalDateTime.now()
        );
        ownerMember.setStatus(GroupMemberStatus.ACCEPTED);
        toCreate.addMember(ownerMember);
        ImageFile createdImage = fileService.storeFile(createGroupDTO.getImageFile());
        toCreate.setImageFile(createdImage);
        return groupRepositoryJpa.save(toCreate);
    }

    @Override
    public Long delete(Long id) {
        EventGroup toDelete = get(id);
        groupRepositoryJpa.delete(toDelete);
        return id;
    }

    @Transactional(readOnly = true)
    @Override
    public EventGroup get(Long id) {
        Optional<EventGroup> foundGroup = groupRepositoryJpa.findById(id);
        if (foundGroup.isEmpty()) {
            throw new NoEntityFoundException("No such group");
        }
        return foundGroup.get();
    }

    @Override
    public EventGroup update(UpdateGroupDTO updateGroupDTO) {
        EventGroup eventGroup = get(updateGroupDTO.getId());
        BeanUtils.copyProperties(updateGroupDTO, eventGroup);
        return groupRepositoryJpa.save(eventGroup);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventGroup> getEventGroupsByPage(Long userId, int pageSize, int pageNumber) {
        Page<EventGroup> groupPage = groupRepositoryJpa.findAll(PageRequest.of(pageNumber, pageSize));
        return groupPage.stream().toList();
    }

    @Override
    public GroupMember invite(CreateMemberDTO createMemberDTO) {
        return createMember(createMemberDTO, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventGroup> getUserGroups(Long userId) {
        User currentUser = userService.getById(userId);
        return currentUser
                .getMemberships()
                .stream()
                .map(GroupMember::getEventGroup)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventGroup> getGroupsForUserToJoin(Long userId) {
        User currentUser = userService.getById(userId);
        List<EventGroup> allGroups = groupRepositoryJpa.findAll();
        return allGroups.stream().filter(eventGroup -> {
            return !eventGroup.hasUserInteracted(currentUser.getId());
        }).toList();
    }

    @Override
    public GroupMember acceptInvitation(Long groupMemberId) {
        return setMembership(groupMemberId, GroupMemberStatus.ACCEPTED, true);
    }

    @Override
    public GroupMember rejectInvitation(Long groupMemberId) {
        return setMembership(groupMemberId, GroupMemberStatus.REJECTED, true);
    }

//    private GroupMember decideInvitation(Long groupMemberId, GroupMemberStatus groupMemberStatus) {
//        Optional<GroupMember> groupMember = groupMemberRepositoryJpa.findById(groupMemberId);
//        if (groupMember.isEmpty()) {
//            throw new NoEntityFoundException("No membership invitation found.");
//        }
//
//        if (!groupMember.get().getIsInvited()) {
//            throw new InvalidOperationException("User cannot accept when not invited.");
//        }
//
//        groupMember.get().setStatus(groupMemberStatus);
//        groupMember.get().setDecidedAt(LocalDateTime.now());
//        groupMemberRepositoryJpa.save(groupMember.get());
//        return groupMember.get();
//    }

    private GroupMember setMembership(Long groupMemberId, GroupMemberStatus groupMemberStatus, boolean isInvitation) {
        GroupMember currentMember = getGroupMemberById(groupMemberId);

        if (currentMember.getDecidedAt() != null) {
            throw new InvalidOperationException("Membership is already decided.");
        }

        if (isInvitation && !currentMember.getIsInvited()) {
            throw new InvalidOperationException("User cannot accept when not invited.");
        }

        if (!isInvitation && currentMember.getIsInvited()) {
            throw new InvalidOperationException("User cannot accept when not invited.");
        }

        currentMember.setStatus(groupMemberStatus);
        currentMember.setDecidedAt(LocalDateTime.now());
        return groupMemberRepositoryJpa.save(currentMember);
    }

    private GroupMember createMember(CreateMemberDTO createMemberDTO, boolean isInvited) {
        EventGroup eventGroup = get(createMemberDTO.getGroupId());
        User toMakeMember = userService.getById(createMemberDTO.getUserId());
        boolean isMemberExisting = eventGroup
                .getMembers()
                .stream()
                .anyMatch(groupMember -> groupMember.getUser().getId().equals(toMakeMember.getId()));

        if (isMemberExisting) {
            throw new DuplicateResourceException("User already is a member.");
        }
        GroupMember newGroupMember = new GroupMember(
                toMakeMember,
                eventGroup,
                GroupRole.valueOf(createMemberDTO.getRole()),
                isInvited
        );
        newGroupMember.setStatus(GroupMemberStatus.PENDING);
        return groupMemberRepositoryJpa.save(newGroupMember);
    }

    @Override
    public GroupMember request(CreateMemberDTO createMemberDTO) {
        return createMember(createMemberDTO, false);
    }

    @Override
    public GroupMember rejectRequest(Long groupMemberId) {
        return setMembership(groupMemberId, GroupMemberStatus.REJECTED, false);
    }

    @Override
    public GroupMember acceptRequest(Long groupMemberId) {
        return setMembership(groupMemberId, GroupMemberStatus.ACCEPTED, false);
    }

    @Override
    public GroupMember removeMember(Long groupMemberId) {
        GroupMember currentMember = getGroupMemberById(groupMemberId);
        groupMemberRepositoryJpa.delete(currentMember);
        return currentMember;
    }

    @Override
    public GroupMember modifyMember(Long groupMemberId, GroupRole groupRole) {
        GroupMember currentMember = getGroupMemberById(groupMemberId);
        currentMember.setGroupRole(groupRole);
        return groupMemberRepositoryJpa.save(currentMember);
    }

    @Transactional(readOnly = true)
    @Override
    public GroupMember getGroupMemberById(Long id) {
        Optional<GroupMember> groupMember = groupMemberRepositoryJpa.findById(id);
        if (groupMember.isEmpty()) {
            throw new NoEntityFoundException("No membership invitation found.");
        }
        return groupMember.get();
    }

    @Transactional(readOnly = true)
    @Override
    public GroupMember getGroupMemberByGroupAndUser(Long userId, Long groupId) {
        Optional<GroupMember> groupMember = groupMemberRepositoryJpa.findByUserIdAndEventGroupId(userId, groupId);
        if (groupMember.isEmpty()) {
            throw new NoEntityFoundException("No membership invitation found.");
        }
        return groupMember.get();
    }

}
