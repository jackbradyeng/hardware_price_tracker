package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.RAMDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.RAMPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.RAMPricePointRepository;
import com.price_tracker.services.price_point_services.RAMPricePointService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RAMPricePointServiceImpl implements RAMPricePointService {

    private final RAMPricePointRepository ramPricePointRepository;
    private final GenericMapper<RAMPricePoint, RAMPricePointDTO> ramPricePointMapper;
    private final GenericMapper<RAMEntity, RAMDTO> ramMapper;

    @Autowired
    public RAMPricePointServiceImpl(RAMPricePointRepository ramPricePointRepository,
                                    MapperFactory mapperFactory) {
        this.ramPricePointRepository = ramPricePointRepository;
        this.ramPricePointMapper = mapperFactory.create(RAMPricePoint.class, RAMPricePointDTO.class);
        this.ramMapper = mapperFactory.create(RAMEntity.class, RAMDTO.class);
    }

    @Override
    public Page<RAMPricePointDTO> findAll(Pageable pageable) {
        return ramPricePointRepository.findAll(pageable)
                .map(ramPricePointMapper::mapTo);
    }

    @Override
    public Optional<RAMDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<RAMDataAndPricePointProjection> resultList = ramPricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        // if list is empty return a 404
        if(resultList.isEmpty()) {
            return Optional.empty();
        }

        // convert RAM to a DTO so we can expose it in our API
        RAMEntity ram = resultList.stream().toList().getFirst().getRAMEntity();
        RAMDTO ramDTO = ramMapper.mapTo(ram);

        // convert RAM price points to a list of DTOs
        List<RAMPricePointDTO> ramPricePointDTOS = resultList.stream()
                .map(result -> ramPricePointMapper.mapTo(result.getRAMPricePoint()))
                .toList();

        // construct the return object for our API
        RAMDataAndPricePointDTO ramDataAndPricePointDTO = RAMDataAndPricePointDTO.builder()
                .ramDTO(ramDTO)
                .ramPricePointDTOList(ramPricePointDTOS)
                .page(resultList.getNumber())
                .pageSize(resultList.getSize())
                .totalPages(resultList.getTotalPages())
                .totalElements(resultList.getTotalElements())
                .build();

        return Optional.of(ramDataAndPricePointDTO);
    }
}
