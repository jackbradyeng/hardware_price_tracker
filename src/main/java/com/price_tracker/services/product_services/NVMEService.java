package com.price_tracker.services.product_services;

import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import java.util.List;
import java.util.Optional;

public interface NVMEService {

    NVMEDTO save(NVMEDTO nvmeDTO);

    List<NVMEDTO> saveAll(List<NVMEDTO> nvmeDTOs);

    List<NVMEDTO> findAll();

    Optional<NVMEDTO> findOne(String id);

    Boolean exists(String id);

    Optional<NVMEDTO> fullUpdate(String id, NVMEDTO nvmeDTO);

    Optional<NVMEDTO> partialUpdate(String id, NVMEDTO nvmeDTO);

    void delete(String id);
}