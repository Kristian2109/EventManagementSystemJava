package com.projects.eventsbook.DTO.userDomain;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Value;

public interface UserProjection {
    public String getFirstName();
    @Value("#{target.firstName + ' '  + target.lastName}" )
    public String getFullName();

}
