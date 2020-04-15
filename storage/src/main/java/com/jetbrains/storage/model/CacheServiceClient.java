package com.jetbrains.storage.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;

@FeignClient(name = "cache", fallback = CacheFallback.class)
public interface CacheServiceClient  {

    @GetMapping("/getCachedFilesByWord")
    Collection<String> getCachedFilesByWord(@RequestParam String word);

    @PostMapping("/putCachedFilesByWord")
    void putCachedFilesByWord(@RequestParam String word, @RequestBody Collection<String> files);

    @PostMapping("/updateCachedFiles")
    void updateCachedFilesByWord(@RequestParam String file, @RequestBody Collection<String> words);

}

@Component
class CacheFallback implements CacheServiceClient{

    @Override
    public Collection<String> getCachedFilesByWord(String word) {
        return new ArrayList<>();
    }

    @Override
    public void putCachedFilesByWord(String word, Collection<String> files) {

    }

    @Override
    public void updateCachedFilesByWord(String file, Collection<String> words) {

    }
}
