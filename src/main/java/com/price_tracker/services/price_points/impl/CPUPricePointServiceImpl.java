package com.price_tracker.services.price_points.impl;

import com.price_tracker.domain.dto.price_point_dtos.CPUPricePointDTO;
import com.price_tracker.mappers.price_point_mappers.CPUPricePointMapper;
import com.price_tracker.repositories.price_point_repos.CPUPricePointRepository;
import com.price_tracker.services.price_points.CPUPricePointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CPUPricePointServiceImpl implements CPUPricePointService {

    private final CPUPricePointRepository cpuPricePointRepository;
    private final CPUPricePointMapper cpuPricePointMapper;

    @Override
    public List<CPUPricePointDTO> findAll() {
        return cpuPricePointRepository.findAll().stream()
                .map(cpuPricePointMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<CPUPricePointDTO> findOne(Long id) {
        return cpuPricePointRepository.findById(id)
                .map(cpuPricePointMapper::mapTo);
    }
}
