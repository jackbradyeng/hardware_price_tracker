package com.price_tracker.services.price_points.impl;

import com.price_tracker.domain.dto.GPUPricePointDTO;
import com.price_tracker.mappers.impl.GPUPricePointMapper;
import com.price_tracker.repositories.GPUPricePointRepository;
import com.price_tracker.services.price_points.GPUPricePointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GPUPricePointServiceImpl implements GPUPricePointService {

    private final GPUPricePointRepository gpuPricePointRepository;
    private final GPUPricePointMapper gpuPricePointMapper;

    @Override
    public List<GPUPricePointDTO> findAll() {
        return gpuPricePointRepository.findAll().stream()
                .map(gpuPricePointMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<GPUPricePointDTO> findOne(Long id) {
        return gpuPricePointRepository.findById(id)
                .map(gpuPricePointMapper::mapTo);
    }
}
