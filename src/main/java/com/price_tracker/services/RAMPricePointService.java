package com.price_tracker.services;

import com.price_tracker.domain.entities.RAMPricePoint;

public interface RAMPricePointService {

    RAMPricePoint save(RAMPricePoint ramPricePoint);

    void delete(Long id);
}
