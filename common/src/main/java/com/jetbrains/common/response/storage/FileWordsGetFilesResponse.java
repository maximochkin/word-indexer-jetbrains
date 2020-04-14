package com.jetbrains.common.response.storage;

import java.io.Serializable;
import java.util.Collection;

public class FileWordsGetFilesResponse implements Serializable {
    private Collection<Long> files;

    public FileWordsGetFilesResponse() {
    }

    public FileWordsGetFilesResponse(Collection<Long> files) {
        this.files = files;
    }

    public Collection<Long> getFiles() {
        return files;
    }

    public void setFiles(Collection<Long> files) {
        this.files = files;
    }
}
