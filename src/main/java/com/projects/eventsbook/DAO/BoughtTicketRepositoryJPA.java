package com.projects.eventsbook.DAO;

import com.projects.eventsbook.entity.BoughtTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoughtTicketRepositoryJPA extends JpaRepository<BoughtTicket, Long> {
}
