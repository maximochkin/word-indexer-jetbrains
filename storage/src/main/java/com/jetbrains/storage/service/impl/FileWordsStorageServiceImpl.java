package com.jetbrains.storage.service.impl;

import com.jetbrains.storage.data.WordFileMappingEntity;
import com.jetbrains.storage.model.CacheServiceClient;
import com.jetbrains.storage.repository.WordFileMappingRepository;
import com.jetbrains.storage.service.FileWordsStorageService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileWordsStorageServiceImpl implements FileWordsStorageService {

    private final WordFileMappingRepository wordFileMappingRepository;
    private final CacheServiceClient cacheServiceClient;

    public FileWordsStorageServiceImpl(WordFileMappingRepository wordFileMappingRepository,
                                       CacheServiceClient cacheServiceClient) {
        this.wordFileMappingRepository = wordFileMappingRepository;
        this.cacheServiceClient = cacheServiceClient;
    }

    @Override
    public void save(String file, Collection<String> words) {
        try {
            cacheServiceClient.updateCachedFilesByWord(file, words);
        } catch (RuntimeException e) {
            log.error("Cache is unavailable. Get data from db", e);
        }
        List<WordFileMappingEntity> entities = words.stream()
                .map(word -> new WordFileMappingEntity(file, word))
                .collect(Collectors.toList());
        wordFileMappingRepository.saveAll(entities);
    }

    @Override
    public Collection<String> getFilesByWord(String word) {
        Collection<String> cached = null;
        try {
            cached = cacheServiceClient.getCachedFilesByWord(word);
        } catch (RuntimeException e) {
            log.error("Cache is unavailable. Get data from db", e);
        }
        if (cached == null ||cached.isEmpty()) {
            Collection<String> files = wordFileMappingRepository.findAllByWord(word).stream()
                    .map(WordFileMappingEntity::getFile)
                    .collect(Collectors.toList());
            try {
                cacheServiceClient.putCachedFilesByWord(word, files);
            } catch (RuntimeException e) {
                log.error("Cache is unavailable. Get data from db", e);
            }
            return files;
        } else {
            return cached;
        }
    }

}
