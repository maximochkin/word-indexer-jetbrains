package com.jetbrains.fileuploader.service.impl;

import com.jetbrains.fileuploader.model.StorageServiceClient;
import com.jetbrains.fileuploader.service.UploaderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UploaderServiceImpl implements UploaderService {

    private final StorageServiceClient storageServiceClient;

    public UploaderServiceImpl(StorageServiceClient storageServiceClient) {
        this.storageServiceClient = storageServiceClient;
    }

    @Override
    public void process(String name, MultipartFile file) {
        Collection<String> words = splitIntoWords(file);
        persist(name, words);
    }

    private void persist(String name, Collection<String> words) {
        storageServiceClient.persist(name, words);
    }

    private Collection<String> splitIntoWords(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            Set<String> words = new HashSet<>();
            Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
            Matcher m;
            String line;

            while ((line = reader.readLine()) != null) {
                m = pattern.matcher(line);
                while (m.find()) {
                    words.add(m.group().toLowerCase());
                }
            }
            return words;
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file " + file.getOriginalFilename(), e);
        }
    }


}
