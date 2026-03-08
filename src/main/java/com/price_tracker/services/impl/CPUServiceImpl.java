package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.CPUEntity;
import com.price_tracker.repositories.CPURepository;
import com.price_tracker.services.CPUService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CPUServiceImpl implements CPUService {

    private final CPURepository cpuRepository;

    @Override
    public CPUEntity save(CPUEntity cpuEntity) {
        return cpuRepository.save(cpuEntity);
    }

    @Override
    public List<CPUEntity> saveAll(List<CPUEntity> cpuEntities) {
        return cpuRepository.saveAll(cpuEntities);
    }

    @Override
    public List<CPUEntity> findAll() {
        return cpuRepository.findAll()
                .stream()
                .toList();
    }

    @Override
    public Optional<CPUEntity> findOne(String id) {
        return cpuRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return cpuRepository.existsById(id);
    }

    @Override
    public CPUEntity partialUpdate(String id, CPUEntity cpuEntity) {
        cpuEntity.setModelNumber(id);

        return cpuRepository.findById(id).map(existingCPU -> {
            Optional.ofNullable(cpuEntity.getName()).ifPresent(existingCPU::setName);
            return cpuRepository.save(existingCPU);
        }).orElseThrow(() -> new RuntimeException("CPU does not exist."));
    }

    @Override
    public void delete(String id) {
        cpuRepository.deleteById(id);
    }
}
