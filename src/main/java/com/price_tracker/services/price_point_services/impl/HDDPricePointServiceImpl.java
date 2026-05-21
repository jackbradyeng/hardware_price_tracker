package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.HDDDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.HDDDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.HDDPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.domain.entities.product_entities.HDDEntity;
import com.price_tracker.mappers.price_point_mappers.HDDPricePointMapper;
import com.price_tracker.mappers.product_mappers.HDDMapper;
import com.price_tracker.repositories.price_point_repos.HDDPricePointRepository;
import com.price_tracker.services.price_point_services.HDDPricePointService;
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
public class HDDPricePointServiceImpl implements HDDPricePointService {

    private final HDDPricePointRepository hddPricePointRepository;
    private final HDDPricePointMapper hddPricePointMapper;
    private final HDDMapper hddMapper;

    @Override
    public Page<HDDPricePointDTO> findAll(Pageable pageable) {
        return hddPricePointRepository.findAll(pageable)
                .map(hddPricePointMapper::mapTo);
    }

    @Override
    public Optional<HDDDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<HDDDataAndPricePointProjection> resultList = hddPricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        HDDEntity hdd = resultList.stream().toList().getFirst().getHDDEntity();
        HDDDTO hddDTO = hddMapper.mapTo(hdd);

        List<HDDPricePointDTO> hddPricePointDTOS = resultList.stream()
                .map(result -> hddPricePointMapper.mapTo(result.getHDDPricePoint()))
                .toList();

        HDDDataAndPricePointDTO hddDataAndPricePointDTO = HDDDataAndPricePointDTO.builder()
                .hddDTO(hddDTO)
                .hddPricePointDTOList(hddPricePointDTOS)
                .page(resultList.getNumber())
                .pageSize(resultList.getSize())
                .totalPages(resultList.getTotalPages())
                .totalElements(resultList.getTotalElements())
                .build();

        return Optional.of(hddDataAndPricePointDTO);
    }
}