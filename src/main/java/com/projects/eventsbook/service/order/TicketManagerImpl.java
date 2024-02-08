package com.projects.eventsbook.service.order;

import com.projects.eventsbook.DAO.BoughtTicketRepositoryJPA;
import com.projects.eventsbook.DAO.UserRepository;
import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.TicketActivation;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.util.RetrieveUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketManagerImpl implements TicketManager {
    private final BoughtTicketRepositoryJPA boughtTicketRepository;
    private final UserRepository userRepository;

    public TicketManagerImpl(BoughtTicketRepositoryJPA boughtTicketRepository, UserRepository userRepository) {
        this.boughtTicketRepository = boughtTicketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<BoughtTicket> getUpcomingUserTickets(Long userId) {
        List<BoughtTicket> userOrders = boughtTicketRepository.findAllByBoughtBy_Id(userId);
        return userOrders
                .stream()
                .filter(boughtTicket -> {
                    return boughtTicket
                            .getTicketTemplate()
                            .getEvent()
                            .getEndTime()
                            .getDayOfYear() >= LocalDate.now().getDayOfYear();
                }).collect(Collectors.toList());

    }
}
