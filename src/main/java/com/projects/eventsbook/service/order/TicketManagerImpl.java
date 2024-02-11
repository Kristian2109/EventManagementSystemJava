package com.projects.eventsbook.service.order;

import com.projects.eventsbook.DAO.BoughtTicketRepositoryJPA;
import com.projects.eventsbook.DAO.UserRepository;
import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.GroupMember;
import com.projects.eventsbook.entity.TicketActivation;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import com.projects.eventsbook.service.event.AuthorizationService;
import com.projects.eventsbook.util.RetrieveUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class TicketManagerImpl implements TicketManager {
    private final BoughtTicketRepositoryJPA boughtTicketRepository;
    private final UserRepository userRepository;

    private final AuthorizationService authorizationService;

    public TicketManagerImpl(BoughtTicketRepositoryJPA boughtTicketRepository, UserRepository userRepository, AuthorizationService authorizationService) {
        this.boughtTicketRepository = boughtTicketRepository;
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional(readOnly = true)
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

    @Override
    public void activateTicket(Long ticketId) {
        Optional<TicketActivation> ticketActivation = this.boughtTicketRepository.findOrderByTicketActivationId(ticketId);
        if (ticketActivation.isEmpty()) {
            throw new NoEntityFoundException("No such ticket!");
        }
        boughtTicketRepository.updateTicketActivation(ticketId, LocalDateTime.now());
    }

    @Override
    public String activateTicketByUser(Long eventId, Long ticketId, Long userId) {
        Optional<TicketActivation> ticketActivation = this.boughtTicketRepository.findOrderByTicketActivationId(ticketId);
        if (ticketActivation.isEmpty()) {
            throw new NoEntityFoundException("No such ticket!");
        }
        User activatingUser = RetrieveUtil.getByIdWithException(this.userRepository, userId);
        Optional<GroupMember> member = activatingUser
                .getMemberships()
                .stream()
                .filter(groupMember ->
                    groupMember.getUser().equals(activatingUser))
                .findFirst();
        if (member.isEmpty()) {
            throw new NoEntityFoundException("No such member in this group!");
        }
        this.authorizationService.validateManageMembershipRequest(member.get());
        if (!Objects.equals(ticketActivation.get().getOrder().getTicketTemplate().getEvent().getId(), eventId)) {
            throw new InvalidOperationException("Invalid activation Id!");
        }
        if (ticketActivation.get().getActivationDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return "Ticket was activated on: " + ticketActivation.get().getActivationDate().format(formatter);
        }
        this.activateTicket(ticketId);
        return "Ticket successfully activated";
    }
}
