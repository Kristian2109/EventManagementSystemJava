package com.projects.eventsbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Base64;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "name")
})
public class ImageFile extends IdentityClassBase{
    private String name;
    private String format;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @NotNull
    private byte[] data;

    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", type='" + format + '\'' +
                '}';
    }

    public String getEncoded() {
        return Base64.getEncoder().encodeToString(data);
    }
}
