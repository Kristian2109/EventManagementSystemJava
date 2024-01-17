package com.projects.eventsbook.entity;

import com.projects.eventsbook.enumerations.ProfileStatus;
import com.projects.eventsbook.enumerations.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String password;
    private LocalDate bornAt;
    private String phoneNumber;
    private LocalDateTime createdAt = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role = Role.USER;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProfileStatus profileStatus = ProfileStatus.ACTIVE;
    @OneToMany(mappedBy = "boughtBy", fetch = FetchType.EAGER)
    private List<BoughtTicket> boughtTickets = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<GroupMember> memberships = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<TicketCard> ticketCards = new ArrayList<>();
    private String identityNumber;
    @Lob
    private byte[] photo;

    public User(String firstName, String lastName, String email, String password, String username, LocalDate bornAt, String phoneNumber, String identityNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.bornAt = bornAt;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.identityNumber = identityNumber;
    }

    public User(String email,  String password) {
        this.email = email;
        this.password = password;
    }

    public boolean hasUserBeenOnEvent(Event event) {
        return getBoughtTickets()
                .stream()
                .anyMatch(ticketLine -> {
                    return ticketLine
                            .getTicketTemplate()
                            .getEvent()
                            .equals(event);
                });
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
