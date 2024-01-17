package com.projects.eventsbook.DTO.userDomain;

import com.projects.eventsbook.exceptions.InvalidUserDataException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class LoginUserDTO {
    private String email;
    private String password;
}
