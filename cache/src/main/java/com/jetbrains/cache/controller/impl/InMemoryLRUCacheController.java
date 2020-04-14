package com.jetbrains.cache.controller.impl;

import com.jetbrains.cache.service.FileNamesCacheService;
import com.jetbrains.cache.service.IndexCacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class InMemoryLRUCacheController {

    private final IndexCacheService indexCacheService;
    private final FileNamesCacheService fileNamesCacheService;

    public InMemoryLRUCacheController(IndexCacheService indexCacheService, FileNamesCacheService fileNamesCacheService) {
        this.indexCacheService = indexCacheService;
        this.fileNamesCacheService = fileNamesCacheService;
    }


    @GetMapping("/getCachedFilesByWord")
    public ResponseEntity<Collection<String>> getCachedFilesByWord(@RequestParam String word) {
        Collection<Long> ids = indexCacheService.getFilesByWord(word);
        if (ids == null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ArrayList<>());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(fileNamesCacheService.getFileNamesByIds(ids));
        }
    }

    @PostMapping("/updateCachedFilesByWord")
    public ResponseEntity updateCachedFilesByWord(@RequestParam("word") String word, @RequestBody Collection<Long> fileIds) {
        indexCacheService.updateFilesByWord(word, fileIds);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/getCachedNameById")
    public ResponseEntity<String> getCachedNameById(@RequestParam("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileNamesCacheService.getFileNameById(id));
    }

    @PostMapping("/getCachedNamesByIds") // i know 'post' usage is misleading but i need a body here to decrease number of requests
    public ResponseEntity<Collection<String>> getCachedNamesByIds(@RequestBody Collection<Long> ids) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileNamesCacheService.getFileNamesByIds(ids));
    }

    @PostMapping("/updateCachedNameById")
    public ResponseEntity insertCachedNameById(@RequestParam("id") Long id, @RequestBody String name) {
        fileNamesCacheService.updateFileNameById(name, id);
        return new ResponseEntity(HttpStatus.CREATED);
    }


}
