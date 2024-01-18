package com.projects.eventsbook.entity;

import com.projects.eventsbook.enumerations.ProfileStatus;
import com.projects.eventsbook.enumerations.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "User.relations", attributeNodes = {
        @NamedAttributeNode("boughtTickets"),
        @NamedAttributeNode("memberships"),
        @NamedAttributeNode("ticketCards")
})
public class User extends IdentityClassBase {
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
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role = Role.USER;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProfileStatus profileStatus = ProfileStatus.ACTIVE;
    @OneToMany(mappedBy = "boughtBy")
    private List<BoughtTicket> boughtTickets = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<GroupMember> memberships = new ArrayList<>();
    @OneToMany(mappedBy = "user")
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

    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
