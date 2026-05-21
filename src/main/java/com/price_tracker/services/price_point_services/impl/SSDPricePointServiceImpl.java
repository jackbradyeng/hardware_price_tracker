package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.SSDDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.SSDDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.SSDPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
import com.price_tracker.mappers.price_point_mappers.SSDPricePointMapper;
import com.price_tracker.mappers.product_mappers.SSDMapper;
import com.price_tracker.repositories.price_point_repos.SSDPricePointRepository;
import com.price_tracker.services.price_point_services.SSDPricePointService;
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
public class SSDPricePointServiceImpl implements SSDPricePointService {

    private final SSDPricePointRepository ssdPricePointRepository;
    private final SSDPricePointMapper ssdPricePointMapper;
    private final SSDMapper ssdMapper;

    @Override
    public Page<SSDPricePointDTO> findAll(Pageable pageable) {
        return ssdPricePointRepository.findAll(pageable)
                .map(ssdPricePointMapper::mapTo);
    }

    @Override
    public Optional<SSDDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<SSDDataAndPricePointProjection> resultList = ssdPricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        SSDEntity ssd = resultList.stream().toList().getFirst().getSSDEntity();
        SSDDTO ssdDTO = ssdMapper.mapTo(ssd);

        List<SSDPricePointDTO> ssdPricePointDTOS = resultList.stream()
                .map(result -> ssdPricePointMapper.mapTo(result.getSSDPricePoint()))
                .toList();

        SSDDataAndPricePointDTO ssdDataAndPricePointDTO = SSDDataAndPricePointDTO.builder()
                .ssdDTO(ssdDTO)
                .ssdPricePointDTOList(ssdPricePointDTOS)
                .page(resultList.getNumber())
                .pageSize(resultList.getSize())
                .totalPages(resultList.getTotalPages())
                .totalElements(resultList.getTotalElements())
                .build();

        return Optional.of(ssdDataAndPricePointDTO);
    }
}