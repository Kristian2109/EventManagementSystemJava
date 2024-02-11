package com.projects.eventsbook.entity;

import com.projects.eventsbook.enumerations.GroupMemberStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@NamedEntityGraph(name = "EventGroup.relations", attributeNodes = {
        @NamedAttributeNode("members"),
        @NamedAttributeNode(value = "events", subgraph = "Event.image"),
        @NamedAttributeNode("imageFile"),
}, subgraphs = {
        @NamedSubgraph(name = "Event.image", attributeNodes = {
                @NamedAttributeNode("imageFile")
        })
})
@NamedEntityGraph(name = "Group.image", attributeNodes = {
        @NamedAttributeNode("imageFile")
})
@Table(indexes = {
        @Index(columnList = "name", unique = true)
})
public class EventGroup extends IdentityClassBase{
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User owner;
    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    @Lob
    private String description;
    @NotNull
    private Boolean isPrivate;
    @ManyToOne
    private ImageFile imageFile;
    @Min(0)
    private double balance;
    @OneToMany(mappedBy = "eventGroup", cascade = CascadeType.ALL)
    private List<GroupMember> members = new ArrayList<>();
    @OneToMany(mappedBy = "eventGroup")
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
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
