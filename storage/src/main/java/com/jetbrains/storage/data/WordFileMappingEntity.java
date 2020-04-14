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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "word_file_seq")
    private Long id;

    @Column(nullable = false)
    private Long fileId;

    @Column(nullable = false)
    private String word;

    public WordFileMappingEntity() {
    }

    public WordFileMappingEntity(Long fileId, String word) {
        this.fileId = fileId;
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
