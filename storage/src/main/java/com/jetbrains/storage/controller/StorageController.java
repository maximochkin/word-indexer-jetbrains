package com.jetbrains.storage.controller;

import com.jetbrains.common.response.storage.FileWordsGetFilesResponse;
import com.jetbrains.storage.service.FileWordsStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class StorageController {

    private final FileWordsStorageService fileWordsStorageService;

    public StorageController(FileWordsStorageService fileWordsStorageService) {
        this.fileWordsStorageService = fileWordsStorageService;
    }

    @PostMapping("/persistWords")
    public ResponseEntity persist(@RequestParam String file, @RequestBody Collection<String> words) {
        fileWordsStorageService.save(file, words);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/getFilesByWord")
    public ResponseEntity<FileWordsGetFilesResponse> getFilesByWord(@RequestParam String word) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new FileWordsGetFilesResponse(fileWordsStorageService.getFilesByWord(word)));
    }

}
