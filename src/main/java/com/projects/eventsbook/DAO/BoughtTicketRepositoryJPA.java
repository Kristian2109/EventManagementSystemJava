package com.projects.eventsbook.DAO;

import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.TicketActivation;
import com.projects.eventsbook.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoughtTicketRepositoryJPA extends JpaRepository<BoughtTicket, Long> {
    @EntityGraph("BoughtTicket.relations")
    List<BoughtTicket> findAllByBoughtBy_Id(Long userId);


    @Query("select act from TicketActivation act where act.id = ?1")
    Optional<TicketActivation> findOrderByTicketActivationId(Long activationId);

    @Modifying
    @Query("update TicketActivation act set act.activationDate = ?2 where act.id = ?1")
    void updateTicketActivation(Long activationId, LocalDateTime activationData);
}