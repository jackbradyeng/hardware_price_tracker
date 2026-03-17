package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.price_point_dtos.GPUWorkstationPricePointDTO;
import com.price_tracker.mappers.price_point_mappers.GPUWorkstationPricePointMapper;
import com.price_tracker.repositories.price_point_repos.GPUWorkstationPricePointRepository;
import com.price_tracker.services.price_point_services.GPUWorkstationPricePointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GPUWorkstationPricePointServiceImpl implements GPUWorkstationPricePointService {

    private final GPUWorkstationPricePointRepository gpuWorkstationPricePointRepository;
    private final GPUWorkstationPricePointMapper gpuPricePointMapper;

    @Override
    public List<GPUWorkstationPricePointDTO> findAll() {
        return gpuWorkstationPricePointRepository.findAll()
                .stream()
                .map(gpuPricePointMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<GPUWorkstationPricePointDTO> findOne(Long id) {
        return gpuWorkstationPricePointRepository.findById(id).map(gpuPricePointMapper::mapTo);
    }
}
