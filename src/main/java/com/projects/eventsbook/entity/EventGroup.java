package com.projects.eventsbook.entity;

import com.projects.eventsbook.enumerations.GroupMemberStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User owner;
    @NotNull
    private String name;
    @NotNull
    @Lob
    private String description;
    @NotNull
    private Boolean isPrivate;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne
    private ImageFile imageFile;
    @OneToMany(mappedBy = "eventGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GroupMember> members = new ArrayList<>();
    @OneToMany(mappedBy = "eventGroup", fetch = FetchType.EAGER)
    private List<Event> events = new ArrayList<>();


    public boolean isMember(User user) {
        return user.getMemberships()
                .stream()
                .anyMatch(groupMember -> groupMember.getEventGroup().getId().equals(getId()) &&
                        groupMember.getStatus().equals(GroupMemberStatus.ACCEPTED));
    }

    public boolean canAccessGroup(User user) {
        return !isPrivate || isMember(user);
    }
    public void addMember(GroupMember groupMember) {
        members.add(groupMember);
    }

    public boolean hasUserInteracted(Long userId) {
        return members.stream().anyMatch(member -> {
            return member.getUser().getId().equals(userId);
        });
    }

    @Override
    public String toString() {
        return "EventGroup{" +
                "id=" + id +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
