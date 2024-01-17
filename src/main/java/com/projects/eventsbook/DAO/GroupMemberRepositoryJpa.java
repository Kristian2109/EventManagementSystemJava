package com.projects.eventsbook.DAO;

import com.projects.eventsbook.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupMemberRepositoryJpa extends JpaRepository<GroupMember, Long> {
    public Optional<GroupMember> findByUserIdAndEventGroupId(Long userId, Long eventGroupId);
}
