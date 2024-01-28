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
    private final UserService userService;
    private final FileService fileService;

    public EventGroupServiceImpl(GroupRepositoryJpa groupRepositoryJpa, EventGroupValidator eventGroupValidator, UserService userService, FileService fileService) {
        this.groupRepositoryJpa = groupRepositoryJpa;
        this.eventGroupValidator = eventGroupValidator;
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

}
