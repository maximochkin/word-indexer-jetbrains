package com.jetbrains.storage.controller;

import com.jetbrains.common.response.storage.GetNamesByIdsResponse;
import com.jetbrains.storage.service.FileNameStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class FileNameController {

    private final FileNameStorageService fileNameStorageService;

    public FileNameController(FileNameStorageService fileNameStorageService) {
        this.fileNameStorageService = fileNameStorageService;
    }

    @PostMapping("/getIdByNameAndPersist")
    public ResponseEntity<Long> persist(@RequestParam Long name) {
        Long id = fileNameStorageService.persist(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/getNamesByIds")
    public ResponseEntity<GetNamesByIdsResponse> getNamesForIds(@RequestBody Collection<Long> ids) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GetNamesByIdsResponse(fileNameStorageService.getNamesByIds(ids)));
    }

    @GetMapping("/getNameById")
    public ResponseEntity<String> getNameById(@RequestParam Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileNameStorageService.getNameById(id));
    }
}
