package com.projects.eventsbook.entity;

import com.projects.eventsbook.enumerations.GroupRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@NamedEntityGraph(name = "Event.relations", attributeNodes = {
        @NamedAttributeNode("reviews"),
        @NamedAttributeNode("ticketTemplates"),
        @NamedAttributeNode("imageFile"),
        @NamedAttributeNode("eventGroup")
})
@Table(indexes = {
        @Index(columnList = "name")
})
public class Event extends IdentityClassBase {
    @NotBlank
    private String name;
    @NotBlank
    @Lob
    private String description;
    @NotBlank
    @NotNull
    private LocalDateTime beginTime;
    @NotBlank
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private Boolean isFree;
    @NotNull
    @ManyToOne(optional = false)
    private EventGroup eventGroup;
    @NotNull
    @ManyToOne(optional = false)
    private User createdBy;
    @NotNull
    private Boolean isOnline;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    @OneToMany (mappedBy = "event", cascade = CascadeType.ALL)
    private List<TicketTemplate> ticketTemplates = new ArrayList<>();
    @ManyToOne
    private ImageFile imageFile;
    @ElementCollection
    private List<String> tags = new ArrayList<>();

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

        if (begin >= reviews.size() || reviews.isEmpty()) {
            return new ArrayList<>();
        }
        if (begin + pageSize >= reviews.size()) {
            return reviews.subList(begin, reviews.size());
        }
        return reviews.subList(begin, begin + pageSize);
    }

    public String getImage() {
        return imageFile.getEncoded();
    }

    public Optional<TicketTemplate> getTicketTemplateById(Long ticketTemplateId) {
        return ticketTemplates
                .stream()
                .filter(ticketTemplate -> Objects.equals(ticketTemplate.getId(), ticketTemplateId))
                .findFirst();
    }
}
