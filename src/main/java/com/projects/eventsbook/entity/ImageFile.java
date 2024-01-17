package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String type;
    @Lob
    private byte[] data;


    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getEncoded() {
        return Base64.getEncoder().encodeToString(data);
    }
}
