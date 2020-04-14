package com.jetbrains.storage.service.impl;

import com.jetbrains.storage.data.FileNameIdMappingEntity;
import com.jetbrains.storage.repository.FileNameIdMappingRepository;
import com.jetbrains.storage.service.FileNameStorageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class FileNameStorageServiceImpl implements FileNameStorageService {

    private final FileNameIdMappingRepository repository;

    public FileNameStorageServiceImpl(FileNameIdMappingRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long persist(Long name) {
        return repository.save(new FileNameIdMappingEntity(name)).getId();
    }

    @Override
    public String getNameById(Long id) {
        return repository.findById(id)
                .map(FileNameIdMappingEntity::getName)
                .orElse(null);
    }

    @Override
    public Collection<Long> getNamesByIds(Collection<Long> ids) {
        Collection<Long> names = new ArrayList<>();
        repository.findAllById(ids)
                .forEach(entity -> names.add(entity.getName()));
        return names;
    }
}
