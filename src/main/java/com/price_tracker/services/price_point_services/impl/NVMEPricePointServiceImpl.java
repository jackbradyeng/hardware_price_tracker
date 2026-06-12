package com.price_tracker.services.price_point_services.impl;

import com.price_tracker.domain.dto.hybrid_dtos.NVMEDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_interfaces.NVMEDataAndPricePointProjection;
import com.price_tracker.domain.dto.price_point_dtos.NVMEPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.mappers.price_point_mappers.NVMEPricePointMapper;
import com.price_tracker.repositories.price_point_repos.NVMEPricePointRepository;
import com.price_tracker.services.price_point_services.NVMEPricePointService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NVMEPricePointServiceImpl implements NVMEPricePointService {

    private final NVMEPricePointRepository nvmePricePointRepository;
    private final NVMEPricePointMapper nvmePricePointMapper;
    private final GenericMapper<NVMEEntity, NVMEDTO> nvmeMapper;

    @Autowired
    public NVMEPricePointServiceImpl(NVMEPricePointRepository nvmePricePointRepository,
                                    NVMEPricePointMapper nvmePricePointMapper,
                                    MapperFactory mapperFactory) {
        this.nvmePricePointRepository = nvmePricePointRepository;
        this.nvmePricePointMapper = nvmePricePointMapper;
        this.nvmeMapper = mapperFactory.create(NVMEEntity.class, NVMEDTO.class);
    }

    @Override
    public Page<NVMEPricePointDTO> findAll(Pageable pageable) {
        return nvmePricePointRepository.findAll(pageable)
                .map(nvmePricePointMapper::mapTo);
    }

    @Override
    public Optional<NVMEDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<NVMEDataAndPricePointProjection> resultList = nvmePricePointRepository
                .getPricePointsByModelNumber(modelNumber, pageable);

        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        NVMEEntity nvme = resultList.stream().toList().getFirst().getNVMEEntity();
        NVMEDTO nvmeDTO = nvmeMapper.mapTo(nvme);

        List<NVMEPricePointDTO> nvmePricePointDTOS = resultList.stream()
                .map(result -> nvmePricePointMapper.mapTo(result.getNVMEPricePoint()))
                .toList();

        NVMEDataAndPricePointDTO nvmeDataAndPricePointDTO = NVMEDataAndPricePointDTO.builder()
                .nvmeDTO(nvmeDTO)
                .nvmePricePointDTOList(nvmePricePointDTOS)
                .page(resultList.getNumber())
                .pageSize(resultList.getSize())
                .totalPages(resultList.getTotalPages())
                .totalElements(resultList.getTotalElements())
                .build();

        return Optional.of(nvmeDataAndPricePointDTO);
    }
}