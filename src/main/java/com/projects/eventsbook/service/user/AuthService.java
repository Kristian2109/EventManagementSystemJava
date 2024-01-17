package com.projects.eventsbook.service.user;

import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.DuplicateResourceException;
import com.projects.eventsbook.exceptions.InvalidUserDataException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;

public interface AuthService {
    public User loginUser(User user) throws NoEntityFoundException;

    public User registerUser(User user) throws InvalidUserDataException, DuplicateResourceException;
}
