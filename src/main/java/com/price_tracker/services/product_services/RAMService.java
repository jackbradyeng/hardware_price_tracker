package com.price_tracker.services.product_services;

import com.price_tracker.domain.entities.product_entities.RAMEntity;

import java.util.List;
import java.util.Optional;

public interface RAMService {
    RAMEntity save(RAMEntity ramEntity);

    List<RAMEntity> saveAll(List<RAMEntity> ramEntities);

    List<RAMEntity> findAll();

    Optional<RAMEntity> findOne(String id);

    boolean exists(String id);

    RAMEntity partialUpdate(String id, RAMEntity ramEntity);

    void delete(String id);
}
