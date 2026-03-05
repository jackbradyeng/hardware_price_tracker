package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.CPUPricePoint;
import com.price_tracker.repositories.CPUPricePointRepository;
import com.price_tracker.services.CPUPricePointService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CPUPricePointServiceImpl implements CPUPricePointService {

    private final CPUPricePointRepository cpuPricePointRepository;

    @Override
    public CPUPricePoint save(CPUPricePoint cpuPricePoint) {
        return cpuPricePointRepository.save(cpuPricePoint);
    }

    @Override
    public void delete(Long id) {
        cpuPricePointRepository.deleteById(id);
    }
}
