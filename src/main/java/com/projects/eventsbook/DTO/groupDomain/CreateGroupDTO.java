package com.projects.eventsbook.DTO.groupDomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateGroupDTO {
    private Long ownerId;
    private String name;
    private String description;
    private Boolean isPrivate;
    private MultipartFile imageFile;
}
