package com.jetbrains.searcher.service.impl;

import com.jetbrains.searcher.model.CacheServiceClient;
import com.jetbrains.searcher.model.StorageServiceClient;
import com.jetbrains.searcher.service.SearcherService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SearcherServiceImpl implements SearcherService {

    private final CacheServiceClient cacheServiceClient;
    private final StorageServiceClient storageServiceClient;

    public SearcherServiceImpl(CacheServiceClient cacheServiceClient, StorageServiceClient storageServiceClient) {
        this.cacheServiceClient = cacheServiceClient;
        this.storageServiceClient = storageServiceClient;
    }

    @Override
    public Collection<Long> getFilesByWord(String word) {
        return getFilesByWordFromCache(word);
    }

    private Collection<Long> getFilesByWordFromCache(String word) {
        Collection<Long> filesFromCache = cacheServiceClient.getCachedFilesByWord(word);
        if (filesFromCache.isEmpty()) {
            Collection<Long> filesFromStorage = storageServiceClient.getFilesByWord(word).getFiles();
            cacheServiceClient.updateCachedFilesByWord(word, filesFromCache);
            return filesFromStorage;
        } else {
            return filesFromCache;
        }
    }

}
