package com.projects.eventsbook.service.user;

import com.projects.eventsbook.entity.BoughtTicket;
import com.projects.eventsbook.entity.User;

import java.util.List;

public interface UserService {
    public User update(User user);
    public User getById(Long id);

    public List<BoughtTicket> getUserTickets(Long userId);
}
