package com.projects.eventsbook.DTO.eventDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
    private String name;
    private String description;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Boolean isFree;
    private Long eventGroupId;
    private Long createdById;
    private Boolean isOnline;
    private Long imageDataId;

}
