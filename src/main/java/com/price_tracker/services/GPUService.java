package com.price_tracker.services;

import com.price_tracker.domain.entities.GPU;

import java.util.List;
import java.util.Optional;

public interface GPUService {

    GPU createGPU(GPU gpu);

    List<GPU> findAll();

    Optional<GPU> findOne(String id);
}
