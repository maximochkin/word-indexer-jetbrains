package com.jetbrains.storage.data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FILENAMEIDMAPPING")
public class FileNameIdMappingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "filename_seq")
    private Long id;

    @Column(nullable = false)
    private Long name;

    public FileNameIdMappingEntity() {
    }

    public FileNameIdMappingEntity(Long fileName) {
        this.name=fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }
}
