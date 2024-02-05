package com.projects.eventsbook.entity;

import com.projects.eventsbook.enumerations.ProfileStatus;
import com.projects.eventsbook.enumerations.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
@Table(indexes = {
        @Index(name = "email_idx", columnList = "email", unique = true),
        @Index(name = "username_idx", columnList = "username", unique = true)
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
    @NotBlank
    private String password;
    private LocalDate bornAt;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role = Role.USER;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProfileStatus profileStatus = ProfileStatus.ACTIVE;
    @Column(unique = true)
    private String identityNumber;
    @Min(value = 0)
    private double balance;
    @Lob
    private byte[] photo;
    @OneToMany(mappedBy = "boughtBy")
    private List<BoughtTicket> boughtTickets = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<GroupMember> memberships = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TicketCard> ticketCards = new ArrayList<>();

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

    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public Optional<TicketCard> getTicketCardByTicket(Long ticketId) {
        return ticketCards
                .stream()
                .filter(ticketCard -> ticketCard.getTicketTemplate().getId().equals(ticketId))
                .findFirst();
    }
}
