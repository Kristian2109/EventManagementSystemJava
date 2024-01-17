package com.projects.eventsbook.DTO.groupDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateGroupDTO {
    private Long id;
    private String name;
    private String description;
}
