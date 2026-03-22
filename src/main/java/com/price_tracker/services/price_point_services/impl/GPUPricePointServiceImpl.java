package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import com.price_tracker.mappers.price_point_mappers.GPUPricePointMapper;
import com.price_tracker.mappers.product_mappers.GPUMapper;
import com.price_tracker.repositories.price_point_repos.GPUPricePointRepository;
import com.price_tracker.services.price_point_services.GPUPricePointService;
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
    private final GPUMapper gpuMapper;

    @Override
    public List<GPUPricePointDTO> findAll() {
        return gpuPricePointRepository.findAll().stream()
                .map(gpuPricePointMapper::mapTo)
                .toList();
    }

    @Override
    public List<GPUDataAndPricePointDTO> findByModelNumber(String modelNumber) {

        // we need to map each of the constituent entity and price points to DTOs before exposing them in the API
        return gpuPricePointRepository.getPricePointsByModelNumber(modelNumber).stream()
                .map(projection -> GPUDataAndPricePointDTO.builder()
                        .gpuPricePointDTO(
                                gpuPricePointMapper.mapTo(projection.getGPUPricePoint())
                        )
                        .gpuDTO(
                                gpuMapper.mapTo(projection.getGPUEntity()))
                        .build()
                ).toList();
    }

    @Override
    public Optional<GPUPricePointDTO> findOne(Long id) {
        return gpuPricePointRepository.findById(id)
                .map(gpuPricePointMapper::mapTo);
    }
}
