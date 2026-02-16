package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.RAMPricePoint;
import com.price_tracker.repositories.RAMPricePointRepository;
import com.price_tracker.services.RAMPricePointService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RAMPricePointImpl implements RAMPricePointService {

    private final RAMPricePointRepository ramPricePointRepository;

    @Override
    public RAMPricePoint save(RAMPricePoint ramPricePoint) {
        return ramPricePointRepository.save(ramPricePoint);
    }

    @Override
    public void delete(Long id) {
        ramPricePointRepository.deleteById(id);
    }
}
