package com.price_tracker.services.product_services;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import java.util.List;
import java.util.Optional;

public interface CPUService {

    CPUDTO save(CPUDTO cpuDTO);

    List<CPUDTO> saveAll(List<CPUDTO> cpuDTOs);

    List<CPUDTO> findAll();

    Optional<CPUDTO> findOne(String id);

    Boolean exists(String id);

    Optional<CPUDTO> fullUpdate(String id, CPUDTO cpuDTO);

    Optional<CPUDTO> partialUpdate(String id, CPUDTO cpuDTO);

    void delete(String id);
}