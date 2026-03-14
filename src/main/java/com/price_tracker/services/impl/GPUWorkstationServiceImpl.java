package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.GPUWorkstationEntity;
import com.price_tracker.repositories.GPUWorkstationRepository;
import com.price_tracker.services.GPUWorkstationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GPUWorkstationServiceImpl implements GPUWorkstationService {

    private final GPUWorkstationRepository gpuWorkstationRepository;

    @Override
    public GPUWorkstationEntity save(GPUWorkstationEntity gpuWorkstation) {
        return gpuWorkstationRepository.save(gpuWorkstation);
    }

    @Override
    public List<GPUWorkstationEntity> saveAll(List<GPUWorkstationEntity> gpuWorkstationEntityList) {
        return gpuWorkstationRepository.saveAll(gpuWorkstationEntityList);
    }

    @Override
    public List<GPUWorkstationEntity> findAll() {
        return gpuWorkstationRepository.findAll();
    }

    @Override
    public Optional<GPUWorkstationEntity> findOne(String id) {
        return gpuWorkstationRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return gpuWorkstationRepository.existsById(id);
    }

    @Override
    public GPUWorkstationEntity partialUpdate(String id, GPUWorkstationEntity gpuWorkstation) {
        gpuWorkstation.setModelNumber(id);

        return gpuWorkstationRepository.findById(id).map(existingGPU -> {
            Optional.ofNullable(gpuWorkstation.getName()).ifPresent(existingGPU::setName);
            return gpuWorkstationRepository.save(existingGPU);
        }).orElseThrow(() -> new RuntimeException("WS GPU does not exist."));
    }

    @Override
    public void delete(String id) {
        gpuWorkstationRepository.deleteById(id);
    }
}
