package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.SSDDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.GenericDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.SSDPricePointRepository;
import com.price_tracker.services.price_point_services.SSDPricePointService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SSDPricePointServiceImpl implements SSDPricePointService {

    private final SSDPricePointRepository ssdPricePointRepository;
    private final GenericMapper<SSDPricePoint, GenericPricePointDTO> ssdPricePointMapper;
    private final GenericMapper<SSDEntity, SSDDTO> ssdMapper;

    @Autowired
    public SSDPricePointServiceImpl(SSDPricePointRepository ssdPricePointRepository,
                                    MapperFactory mapperFactory) {
        this.ssdPricePointRepository = ssdPricePointRepository;
        this.ssdPricePointMapper = mapperFactory.create(SSDPricePoint.class, GenericPricePointDTO.class);
        this.ssdMapper = mapperFactory.create(SSDEntity.class, SSDDTO.class);
    }

    @Override
    public Page<GenericPricePointDTO> findAll(Pageable pageable) {
        return ssdPricePointRepository.findAll(pageable)
                .map(ssdPricePointMapper::mapTo);
    }

    @Override
    public Optional<SSDDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<GenericDataAndPricePointProjection<SSDEntity, SSDPricePoint>> resultList = ssdPricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        SSDEntity ssd = resultList.stream().toList().getFirst().getEntity();
        SSDDTO ssdDTO = ssdMapper.mapTo(ssd);

        List<GenericPricePointDTO> ssdPricePointDTOS = resultList.stream()
                .map(result ->
                        ssdPricePointMapper.mapTo(result.getPricePoint()))
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