package com.projects.eventsbook.DAO;

import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoughtTicketRepositoryJPA extends JpaRepository<BoughtTicket, Long> {
    @EntityGraph("BoughtTicket.relations")
    List<BoughtTicket> findAllByBoughtBy_Id(Long userId);
}
