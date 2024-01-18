package com.projects.eventsbook.entity;

import com.projects.eventsbook.enumerations.GroupMemberStatus;
import com.projects.eventsbook.enumerations.GroupRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_event_group", columnNames = {"user", "event_group"})
})
public class GroupMember extends IdentityClassBase{
    @NotNull
    @ManyToOne(optional = false)
    private User user;
    @NotNull
    @ManyToOne(optional = false)
    private EventGroup eventGroup;
    @Enumerated(EnumType.STRING)
    @NotNull
    private GroupRole groupRole;
    @NotNull
    private Boolean isInvited;
    @NotNull
    private LocalDateTime decidedAt;
    @Enumerated(EnumType.STRING)
    @NotNull
    private GroupMemberStatus status;

    public GroupMember(User user, EventGroup eventGroup, GroupRole groupRole,
                       Boolean isInvited, LocalDateTime decidedAt) {
        this.user = user;
        this.eventGroup = eventGroup;
        this.groupRole = groupRole;
        this.isInvited = isInvited;
        this.decidedAt = decidedAt;
    }

    public GroupMember(User user, EventGroup eventGroup, GroupRole groupRole, Boolean isInvited) {
        this.user = user;
        this.eventGroup = eventGroup;
        this.groupRole = groupRole;
        this.isInvited = isInvited;
    }

    @Override
    public String toString() {
        return "GroupMember{" +
                "id=" + getId() +
                ", user=" + user +
                ", eventGroup=" + eventGroup +
                ", groupRole=" + groupRole +
                '}';
    }
}
