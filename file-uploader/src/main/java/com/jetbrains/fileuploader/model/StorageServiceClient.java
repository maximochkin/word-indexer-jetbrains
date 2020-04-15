package com.jetbrains.fileuploader.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(name="storage")
public interface StorageServiceClient {

    @PostMapping("/persistWords")
    ResponseEntity persist(@RequestParam String file, @RequestBody Collection<String> words);


}
