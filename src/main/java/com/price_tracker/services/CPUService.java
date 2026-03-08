package com.price_tracker.services;

import com.price_tracker.domain.entities.CPUEntity;
import java.util.List;
import java.util.Optional;

public interface CPUService {

    CPUEntity save(CPUEntity cpuEntity);

    List<CPUEntity> saveAll(List<CPUEntity> cpuEntities);

    List<CPUEntity> findAll();

    Optional<CPUEntity> findOne(String id);

    boolean exists(String id);

    CPUEntity partialUpdate(String id, CPUEntity cpuEntity);

    void delete(String id);
}
