package com.jetbrains.searcher.controller;

import com.jetbrains.searcher.service.SearcherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class SearcherController {

    private final SearcherService searcherService;

    public SearcherController(SearcherService searcherService) {
        this.searcherService = searcherService;
    }

    @GetMapping("/getFilesByWord")
    public ResponseEntity<Collection<String>> getFilesByWord(@RequestParam String word){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(searcherService.getFilesByWord(word));
    }
}
