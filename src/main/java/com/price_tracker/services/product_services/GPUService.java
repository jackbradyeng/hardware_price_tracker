package com.price_tracker.services.product_services;

import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import java.util.List;
import java.util.Optional;

public interface GPUService {

    GPUDTO save(GPUDTO gpuDTO);

    List<GPUDTO> saveAll(List<GPUDTO> gpuDTOs);

    List<GPUDTO> findAll();

    Optional<GPUDTO> findOne(String id);

    Boolean exists(String id);

    Optional<GPUDTO> fullUpdate(String id, GPUDTO gpuDTO);

    Optional<GPUDTO> partialUpdate(String id, GPUDTO gpuDTO);

    void delete(String id);
}