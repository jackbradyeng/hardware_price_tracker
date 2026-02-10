package com.price_tracker.services.impl;

import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.domain.entities.GPU;
import com.price_tracker.repositories.GPURepository;
import com.price_tracker.services.GPUService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class GPUServiceImpl implements GPUService {

    private final GPURepository gpuRepository;

    public GPUServiceImpl(GPURepository gpuRepository) {
        this.gpuRepository = gpuRepository;
    }

    @Override
    public GPU save(GPU gpu) {
        return gpuRepository.save(gpu);
    }

    @Override
    public List<GPU> findAll() {
        return StreamSupport
                .stream(gpuRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Optional<GPU> findOne(String id) {
        return gpuRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return gpuRepository.existsById(id);
    }

    @Override
    public GPU partialUpdate(String id, GPU gpu) {
        gpu.setModelNumber(id);

        // first retrieve gpu instance, then update and save
        return gpuRepository.findById(id).map(existingGPU -> {
            Optional.ofNullable(gpu.getPrice()).ifPresent(existingGPU::setPrice);
            return gpuRepository.save(existingGPU);
        }).orElseThrow(() -> new RuntimeException("GPU does not exist."));
    }
}
