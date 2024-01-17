package com.projects.eventsbook.service.user;

import com.projects.eventsbook.entity.User;

public interface UserService {
    public User update(User user);
    public User getById(Long id);
}
