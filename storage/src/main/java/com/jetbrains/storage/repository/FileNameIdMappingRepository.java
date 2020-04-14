package com.jetbrains.storage.repository;

import com.jetbrains.storage.data.FileNameIdMappingEntity;
import org.springframework.data.repository.CrudRepository;

public interface FileNameIdMappingRepository extends CrudRepository<FileNameIdMappingEntity, Long> {
}
