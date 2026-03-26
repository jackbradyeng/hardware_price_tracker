package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.GPUWorkstationDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.GPUWorkstationDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.GPUWorkstationPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.mappers.price_point_mappers.GPUWorkstationPricePointMapper;
import com.price_tracker.mappers.product_mappers.GPUWorkstationMapper;
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
    private final GPUWorkstationMapper gpuWorkstationMapper;

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

    @Override
    public GPUWorkstationDataAndPricePointDTO findByModelNumber(String modelNumber) {

        List<GPUWorkstationDataAndPricePointProjection> resultList = gpuWorkstationPricePointRepository
                .getPricePointsByModelNumber(modelNumber);

        // if list is empty return a 404
        if (resultList.isEmpty()) {
            return null;
        }

        // convert GPUWorkstation to a DTO so we can expose it in our API
        GPUWorkstationEntity gpuWorkstation = resultList.getFirst().getGPUWorkstationEntity();
        GPUWorkstationDTO gpuWorkstationDTO = gpuWorkstationMapper.mapTo(gpuWorkstation);

        // convert GPUWorkstation price points to a list of DTOs
        List<GPUWorkstationPricePointDTO> gpuWorkstationPricePointDTOS = resultList.stream()
                .map(result -> gpuPricePointMapper.mapTo(result.getGPUWorkstationPricePoint()))
                .toList();

        return GPUWorkstationDataAndPricePointDTO.builder()
                .gpuWorkstationDTO(gpuWorkstationDTO)
                .gpuWorkstationPricePointDTOList(gpuWorkstationPricePointDTOS)
                .build();
    }
}
