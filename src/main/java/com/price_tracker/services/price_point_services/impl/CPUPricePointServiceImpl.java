package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.CPUDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.CPUPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.mappers.price_point_mappers.CPUPricePointMapper;
import com.price_tracker.mappers.product_mappers.CPUMapper;
import com.price_tracker.repositories.price_point_repos.CPUPricePointRepository;
import com.price_tracker.services.price_point_services.CPUPricePointService;
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
    private final CPUMapper cpuMapper;

    @Override
    public List<CPUPricePointDTO> findAll() {
        return cpuPricePointRepository.findAll().stream()
                .map(cpuPricePointMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<CPUDataAndPricePointDTO> findByModelNumber(String modelNumber) {

        List<CPUDataAndPricePointProjection> resultList = cpuPricePointRepository
                .getPricePointsByModelNumber(modelNumber);

        // throw a 404 if not found
        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        // convert CPU to a DTO so we can expose it in our API
        CPUEntity cpu = resultList.getFirst().getCPUEntity();
        CPUDTO cpuDTO = cpuMapper.mapTo(cpu);

        // convert CPU price points to a list of DTOs
        List<CPUPricePointDTO> cpuPricePointDTOS = resultList.stream()
                .map(result -> cpuPricePointMapper.mapTo(result.getCPUPricePoint()))
                .toList();

        return Optional.ofNullable(
                CPUDataAndPricePointDTO.builder()
                .cpuDTO(cpuDTO)
                .cpuPricePointDTOList(cpuPricePointDTOS)
                .build());
    }
}
