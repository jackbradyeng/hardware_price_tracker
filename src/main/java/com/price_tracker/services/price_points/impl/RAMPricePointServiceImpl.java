package com.price_tracker.services.price_points.impl;

import com.price_tracker.domain.dto.RAMPricePointDTO;
import com.price_tracker.mappers.impl.RAMPricePointMapper;
import com.price_tracker.repositories.RAMPricePointRepository;
import com.price_tracker.services.price_points.RAMPricePointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RAMPricePointServiceImpl implements RAMPricePointService {

    private final RAMPricePointRepository ramPricePointRepository;
    private final RAMPricePointMapper ramPricePointMapper;

    @Override
    public List<RAMPricePointDTO> findAll() {
        return ramPricePointRepository.findAll().stream()
                .map(ramPricePointMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<RAMPricePointDTO> findOne(Long id) {
        return ramPricePointRepository.findById(id)
                .map(ramPricePointMapper::mapTo);
    }
}
