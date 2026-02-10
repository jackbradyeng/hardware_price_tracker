package com.price_tracker.services;

import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.domain.entities.GPU;

import java.util.List;
import java.util.Optional;

public interface GPUService {

    GPU save(GPU gpu);

    List<GPU> findAll();

    Optional<GPU> findOne(String id);

    boolean exists(String id);

    GPU partialUpdate(String id, GPU gpu);

    void delete(String id);
}
