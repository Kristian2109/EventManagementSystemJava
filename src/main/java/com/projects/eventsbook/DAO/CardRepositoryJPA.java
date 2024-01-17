package com.projects.eventsbook.DAO;

import com.projects.eventsbook.entity.TicketCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepositoryJPA extends JpaRepository<TicketCard, Long> {
}
