package com.projects.eventsbook.DAO;

import com.projects.eventsbook.DTO.groupDomain.GroupDTO;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepositoryJpa extends JpaRepository<EventGroup, Long> {
    public List<GroupDTO> findAllByNameNot(String name);
}
