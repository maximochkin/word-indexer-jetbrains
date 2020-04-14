package com.jetbrains.storage.service;

import java.util.Collection;

public interface FileNameStorageService {

    Long persist(Long name);

    String getNameById(Long id);

    Collection<Long> getNamesByIds(Collection<Long> ids);
}
