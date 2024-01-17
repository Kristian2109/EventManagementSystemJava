package com.projects.eventsbook.DTO.groupDomain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDTO {
    private Long userId;
    private String username;
    private LocalDateTime memberSince;
    private String role;
}
