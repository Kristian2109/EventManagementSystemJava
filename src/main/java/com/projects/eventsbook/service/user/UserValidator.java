package com.projects.eventsbook.service.user;

import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.InvalidUserDataException;

public interface UserValidator {
    public void validateUser(User user) throws InvalidUserDataException;
    public void validatePassword(String password) throws InvalidUserDataException;
    public void validateUsername(String username) throws InvalidUserDataException;
    public void validateEmail(String email) throws InvalidUserDataException;
    public void validatePhoneNumber(String phoneNumber) throws InvalidUserDataException;
    public void validateName(String name) throws InvalidUserDataException;
    public void validateIdentityNumber(String identityNumber);
}
