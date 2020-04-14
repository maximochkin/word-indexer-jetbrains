package com.jetbrains.storage.service.impl;

import com.jetbrains.storage.data.WordFileMappingEntity;
import com.jetbrains.storage.repository.FileNameIdMappingRepository;
import com.jetbrains.storage.repository.WordFileMappingRepository;
import com.jetbrains.storage.service.FileWordsStorageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileWordsStorageServiceImpl implements FileWordsStorageService {

    private final WordFileMappingRepository wordFileMappingRepository;
    private final FileNameIdMappingRepository fileNameIdMappingRepository;

    public FileWordsStorageServiceImpl(WordFileMappingRepository wordFileMappingRepository,
                                       FileNameIdMappingRepository fileNameIdMappingRepository) {
        this.wordFileMappingRepository = wordFileMappingRepository;
        this.fileNameIdMappingRepository = fileNameIdMappingRepository;
    }

    @Override
    @Transactional
    public void save(Long fileId, Collection<String> words) {
        List<WordFileMappingEntity> entities = words.stream()
                .map(word -> new WordFileMappingEntity(fileId, word))
                .collect(Collectors.toList());
        wordFileMappingRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public Collection<Long> getFilesByWord(String word) {
        Collection<Long> ids = wordFileMappingRepository.findAllByWord(word).stream()
                .map(WordFileMappingEntity::getFileId)
                .collect(Collectors.toList());

        Collection<Long> names = new ArrayList<>();
        fileNameIdMappingRepository.findAllById(ids).forEach(entity -> names.add(entity.getName()));
        return names;
    }

}
