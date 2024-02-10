package com.projects.eventsbook.DTO.userDomain;

import com.projects.eventsbook.DTO.OrganizerInformationDTO;
import com.projects.eventsbook.DTO.TicketLineDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class UserProfileDTO{
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String username;
        private LocalDate bornAt;
        private String phoneNumber;
        private LocalDateTime createdAt;
        private String profileStatus;
        private String role;
        private String identityNumber;
        private Double balance;
        private List<TicketLineDTO> ticketLines;

        public UserProfileDTO(Long id, String firstName, String lastName, String email,
                              String username, LocalDate bornAt, String phoneNumber,
                              LocalDateTime createdAt, String profileStatus, String role,
                              String identityNumber) {
                this.id = id;
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.username = username;
                this.bornAt = bornAt;
                this.phoneNumber = phoneNumber;
                this.createdAt = createdAt;
                this.profileStatus = profileStatus;
                this.role = role;
                this.identityNumber = identityNumber;
        }

        public UserProfileDTO(Long id) {
                this.id = id;
        }
}
