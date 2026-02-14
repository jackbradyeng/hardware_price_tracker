package com.price_tracker.services;

import com.price_tracker.domain.entities.RAMEntity;

import java.util.List;
import java.util.Optional;

public interface RAMService {
    RAMEntity save(RAMEntity ramEntity);

    List<RAMEntity> findAll();

    Optional<RAMEntity> findOne(String id);

    boolean exists(String id);

    RAMEntity partialUpdate(String id, RAMEntity ramEntity);

    void delete(String id);
}
