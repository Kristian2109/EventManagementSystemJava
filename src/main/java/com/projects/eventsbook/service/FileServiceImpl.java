package com.projects.eventsbook.service;

import com.projects.eventsbook.DAO.ImageFileRepositoryJPA;
import com.projects.eventsbook.entity.ImageFile;
import com.projects.eventsbook.exceptions.NoEntityFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    private final ImageFileRepositoryJPA imageFileRepositoryJPA;
    private final Base64.Encoder encoder = Base64.getEncoder();

    @Autowired
    public FileServiceImpl(ImageFileRepositoryJPA imageFileRepositoryJPA) {
        this.imageFileRepositoryJPA = imageFileRepositoryJPA;
    }


    public ImageFile storeFile(MultipartFile file){
        try {
            ImageFile newImage = new ImageFile();
            byte[] imageData = file.getBytes();
            newImage.setData(imageData);
            newImage.setType(file.getContentType());
            newImage.setName(file.getName());
            return imageFileRepositoryJPA.save(newImage);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ImageFile getFileById(Long fileId) {
        Optional<ImageFile> imageFile = imageFileRepositoryJPA.findById(fileId);
        if (imageFile.isEmpty()) {
            throw new NoEntityFoundException("File not found");
        }
        return imageFile.get();
    }

    @Override
    public String encodeImage(ImageFile imageFile) {
        return encoder.encodeToString(imageFile.getData());
    }
}
