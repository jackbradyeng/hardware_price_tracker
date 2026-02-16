package com.price_tracker.services;

import com.price_tracker.domain.entities.GPUPricePoint;

public interface GPUPricePointService {

    GPUPricePoint save(GPUPricePoint gpuPricePoint);

    void delete(Long id);
}
