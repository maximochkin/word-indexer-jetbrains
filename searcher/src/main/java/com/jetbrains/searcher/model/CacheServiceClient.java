package com.jetbrains.searcher.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(name = "cache")
public interface CacheServiceClient  {

    @GetMapping("/getCachedFilesByWord")
    Collection<Long> getCachedFilesByWord(@RequestParam String word);

    @PostMapping("/updateCachedFilesByWord")
    void updateCachedFilesByWord(@RequestParam("word") String word, @RequestBody Collection<Long> fileIds);

}
