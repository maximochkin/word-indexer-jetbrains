package com.jetbrains.searcher.service;

import java.util.Collection;

public interface SearcherService {
    public Collection<String> getFilesByWord(String word);

}
