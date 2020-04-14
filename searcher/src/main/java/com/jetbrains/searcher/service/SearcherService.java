package com.jetbrains.searcher.service;

import java.util.Collection;

public interface SearcherService {
    public Collection<Long> getFilesByWord(String word);

}
