package com.jetbrains.storage.service;

import java.util.Collection;

public interface FileWordsStorageService {
    void save(String file, Collection<String> words);

    Collection<String> getFilesByWord(String word);
}
