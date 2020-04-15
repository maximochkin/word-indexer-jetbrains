package com.jetbrains.fileuploader.controller;

import com.jetbrains.fileuploader.service.UploaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class UploaderController {

    private final UploaderService uploaderService;

    public UploaderController(UploaderService uploaderService) {
        this.uploaderService = uploaderService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        final String name = file.getOriginalFilename();

        uploaderService.process(name, file);
        return ResponseEntity.ok().body("File " + name + " has been processed successfully");
    }
}
