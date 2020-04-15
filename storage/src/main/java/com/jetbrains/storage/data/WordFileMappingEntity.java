package com.jetbrains.storage.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "WORDFILEMAPPING", indexes = { @Index(name = "IDX_WORD", columnList = "word")} )
public class WordFileMappingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String file;

    @Column(nullable = false)
    private String word;

    public WordFileMappingEntity() {
    }

    public WordFileMappingEntity(String file, String word) {
        this.file = file;
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String fileId) {
        this.file = fileId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
