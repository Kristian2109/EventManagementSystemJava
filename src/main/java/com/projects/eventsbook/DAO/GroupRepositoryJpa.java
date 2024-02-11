package com.projects.eventsbook.DAO;

import com.projects.eventsbook.DTO.groupDomain.GroupDTO;
import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.EventGroup;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.User;
import jakarta.persistence.QueryHint;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepositoryJpa extends JpaRepository<EventGroup, Long> {
    List<GroupDTO> findAllByNameNot(String name);
    @Override
    @EntityGraph("EventGroup.relations")
    @NonNull
    Optional<EventGroup> findById(@NonNull Long id);

    @Query("select m from GroupMember m where m.user.id = ?1 and m.eventGroup.id = ?2")
    Optional<GroupMember> findMemberByGroupAndUserId(@Param("userId") Long userId, @Param("groupId") Long groupId);

    @Override
    @NonNull
    @EntityGraph("Group.image")
    List<EventGroup> findAll();
}
