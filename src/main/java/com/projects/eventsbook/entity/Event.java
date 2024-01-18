package com.projects.eventsbook.entity;

import com.projects.eventsbook.enumerations.GroupRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Event extends IdentityClassBase {
    @NotNull
    private String name;
    @NotNull
    @Lob
    private String description;
    @NotNull
    private LocalDateTime beginTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private Boolean isFree;
    @NotNull
    @ManyToOne
    private EventGroup eventGroup;
    @NotNull
    @ManyToOne
    private User createdBy;
    @NotNull
    private Boolean isOnline;
    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            targetEntity = Review.class)
    private List<Review> reviews = new ArrayList<>();
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = TicketTemplate.class)
    private List<TicketTemplate> ticketTemplates = new ArrayList<>();
    @ManyToOne
    private ImageFile imageFile;

    public void addTicketTemplate(TicketTemplate ticketTemplate) {
        ticketTemplates.add(ticketTemplate);
    }

    public void addEventReview(Review review) {
        reviews.add(review);
    }

    public boolean canUserAccessEvent(User user) {
        return getEventGroup().canAccessGroup(user);
    }

    public boolean canUserModifyEvent(User user) {
        return canUserAccessEvent(user) && user.getMemberships().stream().anyMatch(groupMember -> {
            return groupMember.getEventGroup().getId().equals(getEventGroup().getId()) &&
                    groupMember.getGroupRole().equals(GroupRole.ADMIN);
        });
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public List<Review> getByPage(int pageNumber, int pageSize) {
        int begin = (pageNumber - 1) * pageSize;

        if (begin >= reviews.size()) {
            return new ArrayList<>();
        }
        if (begin + pageSize >= reviews.size()) {
            return reviews.subList(begin, reviews.size());
        }
        return reviews.subList(begin, begin + pageSize);
    }
}
