package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.CPUDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.CPUPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.mappers.price_point_mappers.CPUPricePointMapper;
import com.price_tracker.repositories.price_point_repos.CPUPricePointRepository;
import com.price_tracker.services.price_point_services.CPUPricePointService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CPUPricePointServiceImpl implements CPUPricePointService {

    private final CPUPricePointRepository cpuPricePointRepository;
    private final CPUPricePointMapper cpuPricePointMapper;
    private final GenericMapper<CPUEntity, CPUDTO> cpuMapper;

    @Autowired
    public CPUPricePointServiceImpl(CPUPricePointRepository cpuPricePointRepository,
                                    CPUPricePointMapper cpuPricePointMapper,
                                    MapperFactory mapperFactory) {
        this.cpuPricePointRepository = cpuPricePointRepository;
        this.cpuPricePointMapper = cpuPricePointMapper;
        this.cpuMapper = mapperFactory.create(CPUEntity.class, CPUDTO.class);
    }

    @Override
    public Page<CPUPricePointDTO> findAll(Pageable pageable) {
        return cpuPricePointRepository.findAll(pageable)
                .map(cpuPricePointMapper::mapTo);
    }

    @Override
    public Optional<CPUDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<CPUDataAndPricePointProjection> resultList = cpuPricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        // throw a 404 if not found
        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        // convert CPU to a DTO so we can expose it in our API
        CPUEntity cpu = resultList.stream().toList().getFirst().getCPUEntity();
        CPUDTO cpuDTO = cpuMapper.mapTo(cpu);

        // convert CPU price points to a list of DTOs
        List<CPUPricePointDTO> cpuPricePointDTOS = resultList.stream()
                .map(result -> cpuPricePointMapper.mapTo(result.getCPUPricePoint()))
                .toList();

        CPUDataAndPricePointDTO cpuDataAndPricePointDTO = CPUDataAndPricePointDTO.builder()
                .cpuDTO(cpuDTO)
                .cpuPricePointDTOList(cpuPricePointDTOS)
                .page(resultList.getNumber())
                .pageSize(resultList.getSize())
                .totalPages(resultList.getTotalPages())
                .totalElements(resultList.getTotalElements())
                .build();

        return Optional.of(cpuDataAndPricePointDTO);
    }
}
