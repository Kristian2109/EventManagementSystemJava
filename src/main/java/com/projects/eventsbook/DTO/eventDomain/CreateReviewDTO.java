package com.projects.eventsbook.DTO.eventDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewDTO {
    private Long reviewerId;
    private Long eventId;
    private String title;
    private String content;
    private Integer rating;
}
