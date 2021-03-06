package com.jetbrains.cache.service;

import com.jetbrains.common.utils.LRUCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class IndexCacheService {
    private final LRUCache<String, Collection<String>> cache;

    public IndexCacheService(@Value("${cache.word-index.capacity}") int capacity) {
        this.cache = new LRUCache<>(capacity);
    }

    public synchronized Collection<String> getFilesByWord(String word) {
        return cache.get(word);
    }

    public synchronized void updateFilesByWord(String word, Collection<String> files) {
        cache.put(word, files);
    }

    public synchronized void addFileForWord(String word, String file) {
        if (cache.get(word) != null) {
            cache.get(word).add(file);
        }
    }

    public String printCache() {
        return cache.toString();
    }
}
