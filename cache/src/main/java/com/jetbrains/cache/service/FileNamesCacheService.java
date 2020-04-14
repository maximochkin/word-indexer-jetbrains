package com.jetbrains.cache.service;

import com.jetbrains.common.utils.SynchronizedLRUCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FileNamesCacheService {

    private final SynchronizedLRUCache<Long, String> cache;

    public FileNamesCacheService(@Value("${cache.file-names.capacity}") int capacity) {
        this.cache = new SynchronizedLRUCache<>(capacity);
    }

    public synchronized Collection<String> getFileNamesByIds(Collection<Long> ids) {
        return ids.stream()
                .map(cache::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getFileNameById(Long id) {
        return cache.get(id);
    }

    public void updateFileNameById(String name, Long id) {
        cache.put(id, name);
    }
}
