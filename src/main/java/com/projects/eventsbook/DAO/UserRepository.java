package com.projects.eventsbook.DAO;

import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.DTO.userDomain.UserProjection;
import com.projects.eventsbook.entity.TicketCard;
import com.projects.eventsbook.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUsername(String username);
    public Optional<UserProjection> findAllByFirstNameEquals(String firstName);
}
