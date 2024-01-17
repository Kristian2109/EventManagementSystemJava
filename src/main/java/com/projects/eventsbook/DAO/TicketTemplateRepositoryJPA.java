package com.projects.eventsbook.DAO;

import com.projects.eventsbook.entity.TicketTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTemplateRepositoryJPA extends JpaRepository<TicketTemplate, Long> {
}
