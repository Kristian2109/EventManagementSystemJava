package com.projects.eventsbook.service;

import com.projects.eventsbook.entity.ImageFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public ImageFile storeFile(MultipartFile file);
    public ImageFile getFileById(Long fileId);
    public String encodeImage(ImageFile imageFile);
}
