package com.price_tracker.services;

import com.price_tracker.domain.entities.CPUPricePoint;

public interface CPUPricePointService {

    CPUPricePoint save(CPUPricePoint cpuPricePoint);

    void delete(Long id);
}
