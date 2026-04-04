package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.RAMDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.RAMPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.mappers.price_point_mappers.RAMPricePointMapper;
import com.price_tracker.mappers.product_mappers.RAMMapper;
import com.price_tracker.repositories.price_point_repos.RAMPricePointRepository;
import com.price_tracker.services.price_point_services.RAMPricePointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RAMPricePointServiceImpl implements RAMPricePointService {

    private final RAMPricePointRepository ramPricePointRepository;
    private final RAMPricePointMapper ramPricePointMapper;
    private final RAMMapper ramMapper;

    @Override
    public List<RAMPricePointDTO> findAll() {
        return ramPricePointRepository.findAll().stream()
                .map(ramPricePointMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<RAMDataAndPricePointDTO> findByModelNumber(String modelNumber) {

        List<RAMDataAndPricePointProjection> resultList = ramPricePointRepository
                .getPricePointsByModelNumber(modelNumber);

        // if list is empty return a 404
        if(resultList.isEmpty()) {
            return Optional.empty();
        }

        // convert RAM to a DTO so we can expose it in our API
        RAMEntity ram = resultList.getFirst().getRAMEntity();
        RAMDTO ramDTO = ramMapper.mapTo(ram);

        // convert RAM price points to a list of DTOs
        List<RAMPricePointDTO> ramPricePointDTOS = resultList.stream()
                .map(result -> ramPricePointMapper.mapTo(result.getRAMPricePoint()))
                .toList();

        // construct the return object for our API
        return Optional.ofNullable(
                RAMDataAndPricePointDTO.builder()
                .ramDTO(ramDTO)
                .ramPricePointDTOList(ramPricePointDTOS)
                .build());
    }
}
