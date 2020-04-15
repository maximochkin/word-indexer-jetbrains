package com.jetbrains.fileuploader.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploaderService {
    void process(String name, MultipartFile file);
}
