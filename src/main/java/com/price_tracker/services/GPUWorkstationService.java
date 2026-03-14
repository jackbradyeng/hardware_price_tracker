package com.price_tracker.services;

import com.price_tracker.domain.entities.GPUWorkstationEntity;
import java.util.List;
import java.util.Optional;

public interface GPUWorkstationService {

    GPUWorkstationEntity save(GPUWorkstationEntity gpuWorkstation);

    List<GPUWorkstationEntity> saveAll(List<GPUWorkstationEntity> gpuWorkstationEntityList);

    List<GPUWorkstationEntity> findAll();

    Optional<GPUWorkstationEntity> findOne(String id);

    boolean exists(String id);

    GPUWorkstationEntity partialUpdate(String id, GPUWorkstationEntity gpuWorkstation);

    void delete(String id);
}
