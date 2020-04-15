package com.jetbrains.searcher.service.impl;

import com.jetbrains.searcher.model.StorageServiceClient;
import com.jetbrains.searcher.service.SearcherService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SearcherServiceImpl implements SearcherService {

    private final StorageServiceClient storageServiceClient;

    public SearcherServiceImpl(StorageServiceClient storageServiceClient) {
        this.storageServiceClient = storageServiceClient;
    }

    @Override
    public Collection<String> getFilesByWord(String word) {
        Collection<String> filesFromStorage = storageServiceClient.getFilesByWord(word).getFiles();
        return filesFromStorage;
    }

}
