package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.GPUWorkstationDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.GPUWorkstationDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.GPUWorkstationPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.mappers.price_point_mappers.GPUWorkstationPricePointMapper;
import com.price_tracker.repositories.price_point_repos.GPUWorkstationPricePointRepository;
import com.price_tracker.services.price_point_services.GPUWorkstationPricePointService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GPUWorkstationPricePointServiceImpl implements GPUWorkstationPricePointService {

    private final GPUWorkstationPricePointRepository gpuWorkstationPricePointRepository;
    private final GPUWorkstationPricePointMapper gpuPricePointMapper;
    private final GenericMapper<GPUWorkstationEntity, GPUWorkstationDTO> gpuWorkstationMapper;

    @Autowired
    public GPUWorkstationPricePointServiceImpl(GPUWorkstationPricePointRepository gpuWorkstationPricePointRepository,
                                    GPUWorkstationPricePointMapper gpuPricePointMapper,
                                    MapperFactory mapperFactory) {
        this.gpuWorkstationPricePointRepository = gpuWorkstationPricePointRepository;
        this.gpuPricePointMapper = gpuPricePointMapper;
        this.gpuWorkstationMapper = mapperFactory.create(GPUWorkstationEntity.class, GPUWorkstationDTO.class);
    }

    @Override
    public Page<GPUWorkstationPricePointDTO> findAll(Pageable pageable) {
        return gpuWorkstationPricePointRepository.findAll(pageable)
                .map(gpuPricePointMapper::mapTo);
    }

    @Override
    public Optional<GPUWorkstationDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<GPUWorkstationDataAndPricePointProjection> resultList = gpuWorkstationPricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        // if list is empty return a 404
        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        // convert GPUWorkstation to a DTO so we can expose it in our API
        GPUWorkstationEntity gpuWorkstation = resultList.stream().toList().getFirst().getGPUWorkstationEntity();
        GPUWorkstationDTO gpuWorkstationDTO = gpuWorkstationMapper.mapTo(gpuWorkstation);

        // convert GPUWorkstation price points to a list of DTOs
        List<GPUWorkstationPricePointDTO> gpuWorkstationPricePointDTOS = resultList.stream()
                .map(result -> gpuPricePointMapper.mapTo(result.getGPUWorkstationPricePoint()))
                .toList();

        GPUWorkstationDataAndPricePointDTO gpuWorkstationDataAndPricePointDTO = GPUWorkstationDataAndPricePointDTO.builder()
                .gpuWorkstationDTO(gpuWorkstationDTO)
                .gpuWorkstationPricePointDTOList(gpuWorkstationPricePointDTOS)
                .page(resultList.getNumber())
                .pageSize(resultList.getSize())
                .totalPages(resultList.getTotalPages())
                .totalElements(resultList.getTotalElements())
                .build();

        return Optional.of(gpuWorkstationDataAndPricePointDTO);
    }
}
