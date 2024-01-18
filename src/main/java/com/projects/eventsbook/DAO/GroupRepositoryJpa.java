package com.projects.eventsbook.DAO;

import com.projects.eventsbook.DTO.groupDomain.GroupDTO;
import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepositoryJpa extends JpaRepository<EventGroup, Long> {
    List<GroupDTO> findAllByNameNot(String name);
    @Override
    @EntityGraph("EventGroup.relations")
    @NonNull
    Optional<EventGroup> findById(@NonNull Long id);
}
