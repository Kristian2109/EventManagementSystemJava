package com.projects.eventsbook.util;

import com.projects.eventsbook.entity.IdentityClassBase;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class RetrieveUtil {
    public static <T extends IdentityClassBase> T getByIdWithException(JpaRepository<T, Long> repository, Long id) {
        Optional<T> foundEntity = repository.findById(id);
        if (foundEntity.isEmpty()) {
            throw new NoEntityFoundException("No entity from type " + repository.getClass().getTypeName() + " found");
        }
        return foundEntity.get();
    }
}
