package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @ManyToOne
    private User user;
    @NotNull
    @ManyToOne
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
