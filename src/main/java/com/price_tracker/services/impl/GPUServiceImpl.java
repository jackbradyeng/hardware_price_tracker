package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.GPU;
import com.price_tracker.repositories.GPURepository;
import com.price_tracker.services.GPUService;
import org.springframework.stereotype.Service;

@Service
public class GPUServiceImpl implements GPUService {

    private final GPURepository gpuRepository;

    public GPUServiceImpl(GPURepository gpuRepository) {
        this.gpuRepository = gpuRepository;
    }

    @Override
    public GPU createGPU(GPU gpu) {
        return gpuRepository.save(gpu);
    }
}
