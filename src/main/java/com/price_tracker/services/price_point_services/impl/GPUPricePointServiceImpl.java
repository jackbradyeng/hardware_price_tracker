package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.GPUDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.mappers.price_point_mappers.GPUPricePointMapper;
import com.price_tracker.mappers.product_mappers.GPUMapper;
import com.price_tracker.repositories.price_point_repos.GPUPricePointRepository;
import com.price_tracker.services.price_point_services.GPUPricePointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<GPUPricePointDTO> findAll(Pageable pageable) {
        return gpuPricePointRepository.findAll(pageable)
                .map(gpuPricePointMapper::mapTo);
    }

    @Override
    public Optional<GPUDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<GPUDataAndPricePointProjection> resultList = gpuPricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        // throw a 404 if not found
        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        // convert GPU to a DTO so we can expose it in our API
        GPUEntity gpu = resultList.stream().toList().getFirst().getGPUEntity();
        GPUDTO gpuDTO = gpuMapper.mapTo(gpu);

        // convert GPU price points to a list of DTOs
        List<GPUPricePointDTO> gpuPricePointDTOS = resultList.stream()
                .map(result -> gpuPricePointMapper.mapTo(result.getGPUPricePoint()))
                .toList();

        GPUDataAndPricePointDTO gpuDataAndPricePointDTO = GPUDataAndPricePointDTO.builder()
                .gpuDTO(gpuDTO)
                .gpuPricePointDTOList(gpuPricePointDTOS)
                .page(resultList.getNumber())
                .pageSize(resultList.getSize())
                .totalPages(resultList.getTotalPages())
                .totalElements(resultList.getTotalElements())
                .build();

        return Optional.ofNullable(gpuDataAndPricePointDTO);
    }
}
