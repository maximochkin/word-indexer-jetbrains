package com.jetbrains.storage.repository;

import com.jetbrains.storage.data.WordFileMappingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface WordFileMappingRepository extends CrudRepository<WordFileMappingEntity, Long> {
    Collection<WordFileMappingEntity> findAllByWord(String word);
}
