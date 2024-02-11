package com.projects.eventsbook.service.user;

import com.projects.eventsbook.DAO.UserRepository;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.DuplicateResourceException;
import com.projects.eventsbook.exceptions.InvalidUserDataException;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserValidator userValidatorService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, UserValidator userValidatorService) {
        this.userRepository = userRepository;
        this.userValidatorService = userValidatorService;
    }
    @Override
    @Transactional(readOnly = true)
    public User loginUser(User user) {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser.isEmpty()) {
            throw new NoEntityFoundException("No user with this email");
        }
        String passwordHash = foundUser.get().getPassword();
        if (!BCrypt.checkpw(user.getPassword(), passwordHash)) {
            throw new InvalidUserDataException("Invalid password");
        }
        return foundUser.get();
    }

    @Override
    @Transactional
    public User registerUser(User user) throws InvalidUserDataException, DuplicateResourceException {
        userValidatorService.validateUser(user);
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new InvalidUserDataException("There is an existing user with this email.");
        }

        Optional<User> existingUserByUsername = userRepository.findByEmail(user.getEmail());
        if (existingUserByUsername.isPresent()) {
            throw new InvalidUserDataException("There is an existing user with this username.");
        }

        String salt = BCrypt.gensalt();
        String passwordHash = BCrypt.hashpw(user.getPassword(), salt);
        user.setBalance(0D);
        user.setPassword(passwordHash);
        return userRepository.save(user);
    }
}
