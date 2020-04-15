package com.jetbrains.cache.controller.impl;

import com.jetbrains.cache.service.IndexCacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
public class InMemoryLRUCacheController {

    private final IndexCacheService indexCacheService;

    public InMemoryLRUCacheController(IndexCacheService indexCacheService) {
        this.indexCacheService = indexCacheService;
    }


    @GetMapping("/getCachedFilesByWord")
    public ResponseEntity<Collection<String>> getCachedFilesByWord(@RequestParam String word) {
        Collection<String> ids = indexCacheService.getFilesByWord(word);
        if (ids == null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ArrayList<>());
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ids);
        }
    }

    @PostMapping("/putCachedFilesByWord")
    public ResponseEntity putCachedFilesByWord(@RequestParam String word, @RequestBody Collection<String> files) {
        indexCacheService.updateFilesByWord(word, files);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping("/updateCachedFiles")
    public ResponseEntity updateCachedFilesByWord(@RequestParam String file, @RequestBody Collection<String> words) {
        for (String word : words) {
            indexCacheService.addFileForWord(word, file);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/printCache")
    public ResponseEntity<String> printCache() {
        return ResponseEntity.ok().body(indexCacheService.printCache());
    }


}
