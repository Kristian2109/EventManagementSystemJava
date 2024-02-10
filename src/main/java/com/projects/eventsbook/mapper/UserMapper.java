package com.projects.eventsbook.mapper;


import com.projects.eventsbook.DTO.userDomain.LoginUserDTO;
import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.DTO.userDomain.RegisterUserDTO;
import com.projects.eventsbook.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public static User toUser(RegisterUserDTO user) {
        return new User(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getBornAt(),
                user.getPhoneNumber(),
                user.getIdentityNumber());
    }

    public static User toUser(UserProfileDTO user) {
        User result = new User();
        BeanUtils.copyProperties(user, result);
        System.out.println("Provided user: " + user);
        System.out.println("Updated user: " + result);
        return result;
    }

    public static User toUser(LoginUserDTO user) {
        return new User(
                user.getEmail(),
                user.getPassword()
        );
    }

    public static UserProfileDTO toUserProfileDTO(User user) {
        UserProfileDTO userProfile =  new UserProfileDTO();
        BeanUtils.copyProperties(user, userProfile);
        userProfile.setProfileStatus(user.getProfileStatus().toString());
        userProfile.setRole(user.getRole().toString());
        return userProfile;
    }
}
