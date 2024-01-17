package com.projects.eventsbook.service;


import com.projects.eventsbook.DAO.UserRepository;
import com.projects.eventsbook.service.user.AuthService;
import com.projects.eventsbook.service.user.UserValidator;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private UserValidator userValidatorServiceMock;

    @InjectMocks
    private AuthService authService;

//    @Test
//    void registerWithValidUser() {
//        CreateUserDTO createUserDTO = new CreateUserDTO(
//                "Kris",
//                "Petrov",
//                "kris.petrov@gmail.com",
//                "krisko",
//                "123Abc()",
//                "0887786443",
//                LocalDate.of(2002, 1, 1)
//        );
//        when(userRepositoryMock.findByEmail(createUserDTO.getEmail())).thenReturn(Optional.empty());
//        Long id = authService.register(createUserDTO);
//    }
}
