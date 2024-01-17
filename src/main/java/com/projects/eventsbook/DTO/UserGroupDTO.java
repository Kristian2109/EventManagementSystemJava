package com.projects.eventsbook.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupDTO {
    private Long groupId;
    private String groupName;
}
