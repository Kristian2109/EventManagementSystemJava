package com.projects.eventsbook.service.user;


import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.InvalidUserDataException;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserValidatorImpl implements UserValidator {
    @Override
    public void validateUser(@NonNull User user) {
        validateEmail(user.getEmail());
        validateUsername(user.getUsername());
        validatePassword(user.getPassword());
        validatePhoneNumber(user.getPhoneNumber());
        validateName(user.getFirstName());
        validateName(user.getLastName());
        if ((user.getIdentityNumber() != null) || !user.getIdentityNumber().isEmpty()) {
            validateIdentityNumber(user.getIdentityNumber());
        }
    }


    @Override
    public void validatePassword(@NonNull String password) {
        if (password.length() < 6) {
            throw new InvalidUserDataException("Password is shorter than 6 characters.");
        }
//                password.matches("\\d+") &&
        if (!password.matches(".*[a-z].*")) {
            throw new InvalidUserDataException("Password doesn't contain a Lower case character.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidUserDataException("Password doesn't contain an Upper case character");
        }
    }

    @Override
    public void validateUsername(@NonNull String username) {}

    @Override
    public void validateEmail(@NonNull String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidUserDataException("Invalid email");
        }
    }

    @Override
    public void validatePhoneNumber(@NonNull String phoneNumber) {

    }

    @Override
    public void validateName(@NonNull String name) {
        if (!name.matches("^[A-Z][a-z]*$")) {
            throw new InvalidUserDataException("Invalid name");
        }
    }

    @Override
    public void validateIdentityNumber(String identityNumber) {
        if (!identityNumber.matches("^[0-9]*$")) {
            throw new InvalidUserDataException("Identity Number should contain only numbers.");
        }

        if (identityNumber.length() != 10) {
            throw new InvalidUserDataException("Identity Number should be 10 letters long.");
        }
    }


}
