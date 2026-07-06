package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.GenericDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.GPUPricePointRepository;
import com.price_tracker.services.price_point_services.GPUPricePointService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GPUPricePointServiceImpl implements GPUPricePointService {

    private final GPUPricePointRepository gpuPricePointRepository;
    private final GenericMapper<GPUPricePoint, GenericPricePointDTO> gpuPricePointMapper;
    private final GenericMapper<GPUEntity, GPUDTO> gpuMapper;

    @Autowired
    public GPUPricePointServiceImpl(GPUPricePointRepository gpuPricePointRepository,
                                    MapperFactory mapperFactory) {
        this.gpuPricePointRepository = gpuPricePointRepository;
        this.gpuPricePointMapper = mapperFactory.create(GPUPricePoint.class, GenericPricePointDTO.class);
        this.gpuMapper = mapperFactory.create(GPUEntity.class, GPUDTO.class);
    }

    @Override
    public Page<GenericPricePointDTO> findAll(Pageable pageable) {
        return gpuPricePointRepository.findAll(pageable)
                .map(gpuPricePointMapper::mapTo);
    }

    @Override
    public Optional<GPUDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<GenericDataAndPricePointProjection<GPUEntity, GPUPricePoint>> resultList = gpuPricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        // throw a 404 if not found
        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        // convert GPU to a DTO so we can expose it in our API
        GPUEntity gpu = resultList.stream().toList().getFirst().getEntity();
        GPUDTO gpuDTO = gpuMapper.mapTo(gpu);

        // convert GPU price points to a list of DTOs
        List<GenericPricePointDTO> gpuPricePointDTOS = resultList.stream()
                .map(result ->
                        gpuPricePointMapper.mapTo(result.getPricePoint()))
                .toList();

        GPUDataAndPricePointDTO gpuDataAndPricePointDTO = GPUDataAndPricePointDTO.builder()
                .gpuDTO(gpuDTO)
                .gpuPricePointDTOList(gpuPricePointDTOS)
                .page(resultList.getNumber())
                .pageSize(resultList.getSize())
                .totalPages(resultList.getTotalPages())
                .totalElements(resultList.getTotalElements())
                .build();

        return Optional.of(gpuDataAndPricePointDTO);
    }
}