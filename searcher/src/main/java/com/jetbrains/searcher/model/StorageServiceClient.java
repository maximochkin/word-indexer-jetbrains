package com.jetbrains.searcher.model;

import com.jetbrains.common.response.storage.FileWordsGetFilesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(name = "storage")
public interface StorageServiceClient {

    @GetMapping("/getFilesByWord")
    FileWordsGetFilesResponse getFilesByWord(@RequestParam String word);

}
