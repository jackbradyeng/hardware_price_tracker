package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.repositories.GPURepository;
import com.price_tracker.services.GPUService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class GPUServiceImpl implements GPUService {

    private final GPURepository gpuRepository;

    @Override
    public GPUEntity save(GPUEntity gpuEntity) {
        return gpuRepository.save(gpuEntity);
    }

    @Override
    public List<GPUEntity> findAll() {
        return StreamSupport
                .stream(gpuRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Optional<GPUEntity> findOne(String id) {
        return gpuRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return gpuRepository.existsById(id);
    }

    @Override
    public GPUEntity partialUpdate(String id, GPUEntity gpuEntity) {
        gpuEntity.setModelNumber(id);

        // first retrieve gpu instance, then update and save
        return gpuRepository.findById(id).map(existingGPU -> {
            Optional.ofNullable(gpuEntity.getName()).ifPresent(existingGPU::setName);
            return gpuRepository.save(existingGPU);
        }).orElseThrow(() -> new RuntimeException("GPU does not exist."));
    }

    @Override
    public void delete(String id) {
        gpuRepository.deleteById(id);
    }
}
