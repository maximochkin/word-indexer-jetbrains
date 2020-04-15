package com.jetbrains.searcher.model;

import com.jetbrains.common.response.storage.FileWordsGetFilesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "storage")
public interface StorageServiceClient {

    @GetMapping("/getFilesByWord")
    FileWordsGetFilesResponse getFilesByWord(@RequestParam String word);

}
