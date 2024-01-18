package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Review extends IdentityClassBase {
    @NotNull
    @ManyToOne(optional = false)
    private User user;
    @NotNull
    @ManyToOne(optional = false)
    private Event event;
    @NotNull
    @Size(min = 6, max = 50)
    private String title;
    @NotNull
    @Size(min = 6, max = 1000)
    private String content;
    @NotNull
    @Check(constraints = "rating >= 0 AND rating <= 10")
    private Integer rating;

}
