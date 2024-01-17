package com.projects.eventsbook.DTO.groupDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateMemberDTO {
    private Long userId;
    private Long groupId;
    private String role;
}
