package com.projects.eventsbook.service.user;

import com.projects.eventsbook.DAO.BoughtTicketRepositoryJPA;
import com.projects.eventsbook.DAO.UserRepository;
import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.DuplicateResourceException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidatorService;
    private final BoughtTicketRepositoryJPA boughtTicketRepositoryJPA;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidator userValidatorService, BoughtTicketRepositoryJPA boughtTicketRepositoryJPA) {
        this.userRepository = userRepository;
        this.userValidatorService = userValidatorService;
        this.boughtTicketRepositoryJPA = boughtTicketRepositoryJPA;
    }

    @Override
    @Transactional
    public User update(User user) {
        User currentUser = getById(user.getId());
        if (user.getUsername() != null && !user.getUsername().equals(currentUser.getUsername())) {
            userValidatorService.validateUsername(user.getUsername());
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new DuplicateResourceException("Duplicate username");
            }
            currentUser.setUsername(user.getUsername());
        }

        if (user.getFirstName() != null) {
            userValidatorService.validateName(user.getFirstName());
            currentUser.setFirstName(user.getFirstName());
        }

        if (user.getLastName() != null) {
            userValidatorService.validateName(user.getLastName());
            currentUser.setLastName(user.getLastName());
        }

        if (user.getPhoneNumber() != null) {
            userValidatorService.validatePhoneNumber(user.getPhoneNumber());
            currentUser.setPhoneNumber(user.getPhoneNumber());
        }

        if (user.getIdentityNumber() != null) {
            userValidatorService.validateIdentityNumber(user.getIdentityNumber());
            currentUser.setIdentityNumber(user.getIdentityNumber());
        }
        currentUser.setBoughtTickets(new ArrayList<>());
        return userRepository.save(currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new NoEntityFoundException("No such user.");
        }

        return foundUser.get();
    }

    @Override
    public List<BoughtTicket> getUserTickets(Long userId) {
        List<BoughtTicket> orders = boughtTicketRepositoryJPA.findAllByBoughtBy_Id(userId);
        orders.sort((BoughtTicket first, BoughtTicket second) -> {
            if (first.getCreatedAt().isBefore(second.getCreatedAt())) {
                return 1;
            } else if (first.getCreatedAt().isAfter(second.getCreatedAt())){
                return -1;
            } else {
                return 0;
            }
        });

        return orders;
    }
}
