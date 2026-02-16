package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.repositories.GPUPricePointRepository;
import com.price_tracker.services.GPUPricePointService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GPUPricePointImpl implements GPUPricePointService {

    private final GPUPricePointRepository gpuPricePointRepository;

    @Override
    public GPUPricePoint save(GPUPricePoint gpuPricePoint) {
        return gpuPricePointRepository.save(gpuPricePoint);
    }

    @Override
    public void delete(Long id) {
        gpuPricePointRepository.deleteById(id);
    }
}
