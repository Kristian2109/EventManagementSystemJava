package com.projects.eventsbook.service;

import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.InvalidUserDataException;
import com.projects.eventsbook.service.user.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class UserValidatorTest {

    @Autowired
    private UserValidator userValidatorService;

    @Test
    void onValidUserData() {
        User User = new User(
                "Kris",
                "Petrov",
                "kris.petrov@gmail.com",
                "krisko",
                "123Abc()",
                LocalDate.of(2002, 1, 1),
                "0887786443"
        );

        Assertions.assertDoesNotThrow(() -> userValidatorService.validateUser(User));
    }

    @Test
    void onInvalidEmail() {
        Assertions.assertThrows(InvalidUserDataException.class, () -> userValidatorService.validateEmail("pesho123.abv"));
    }

    @Test
    void onInvalidPassword() {
        Assertions.assertThrows(InvalidUserDataException.class, () -> userValidatorService.validatePassword("123abc"));
    }

    @Test
    void onInvalidNameWithLowerCaseOnly() {
        Assertions.assertThrows(InvalidUserDataException.class, () -> userValidatorService.validateName("ivan"));
    }

    @Test
    void onInvalidNameWithUpperCase() {
        Assertions.assertThrows(InvalidUserDataException.class, () -> userValidatorService.validateName("IVan"));
    }
}
