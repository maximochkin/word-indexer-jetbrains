package com.jetbrains.storage.controller;

import com.jetbrains.common.response.storage.FileWordsGetFilesResponse;
import com.jetbrains.storage.service.FileNameStorageService;
import com.jetbrains.storage.service.FileWordsStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class FileWordsStorageController {

    private final FileWordsStorageService fileWordsStorageService;
    private final FileNameStorageService fileNameStorageService;

    public FileWordsStorageController(FileWordsStorageService fileWordsStorageService, FileNameStorageService fileNameStorageService) {
        this.fileWordsStorageService = fileWordsStorageService;
        this.fileNameStorageService = fileNameStorageService;
    }

    @PostMapping("/persistWords")
    public ResponseEntity persist(@RequestParam Long fileId, @RequestBody Collection<String> words) {
        fileWordsStorageService.save(fileId, words);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/getFilesByWord")
    public ResponseEntity<FileWordsGetFilesResponse> getFilesByWord(@RequestParam String word) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new FileWordsGetFilesResponse(fileWordsStorageService.getFilesByWord(word)));
    }

}
