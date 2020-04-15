package com.jetbrains.common.response.storage;

import java.io.Serializable;
import java.util.Collection;

public class FileWordsGetFilesResponse implements Serializable {
    private Collection<String> files;

    public FileWordsGetFilesResponse() {
    }

    public FileWordsGetFilesResponse(Collection<String> files) {
        this.files = files;
    }

    public Collection<String> getFiles() {
        return files;
    }

    public void setFiles(Collection<String> files) {
        this.files = files;
    }
}
