package com.price_tracker.services;

import com.price_tracker.domain.entities.GPUEntity;
import java.util.List;
import java.util.Optional;

public interface GPUService {

    GPUEntity save(GPUEntity gpuEntity);

    List<GPUEntity> findAll();

    Optional<GPUEntity> findOne(String id);

    boolean exists(String id);

    GPUEntity partialUpdate(String id, GPUEntity gpuEntity);

    void delete(String id);
}
