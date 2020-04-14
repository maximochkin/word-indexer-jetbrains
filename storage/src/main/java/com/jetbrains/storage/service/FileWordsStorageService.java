package com.jetbrains.storage.service;

import java.util.Collection;

public interface FileWordsStorageService {
    void save(Long fileId, Collection<String> words);

    Collection<Long> getFilesByWord(String word);
}
