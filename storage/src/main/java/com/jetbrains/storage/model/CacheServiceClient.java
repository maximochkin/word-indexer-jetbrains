package com.jetbrains.storage.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(name = "cache")
public interface CacheServiceClient  {

    @GetMapping("/getCachedFilesByWord")
    Collection<String> getCachedFilesByWord(@RequestParam String word);

    @PostMapping("/putCachedFilesByWord")
    void putCachedFilesByWord(@RequestParam String word, @RequestBody Collection<String> files);

    @PostMapping("/updateCachedFiles")
    ResponseEntity updateCachedFilesByWord(@RequestParam String file, @RequestBody Collection<String> words);

}
