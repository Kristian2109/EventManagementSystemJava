package com.projects.eventsbook.DAO;

import com.projects.eventsbook.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepositoryJPA extends JpaRepository<ImageFile, Long> {
}
