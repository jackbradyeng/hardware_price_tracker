package com.price_tracker.services.product_services;

import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import java.util.List;
import java.util.Optional;

public interface RAMService {

    RAMDTO save(RAMDTO ramDTO);

    List<RAMDTO> saveAll(List<RAMDTO> ramDTOs);

    List<RAMDTO> findAll();

    Optional<RAMDTO> findOne(String id);

    Boolean exists(String id);

    Optional<RAMDTO> fullUpdate(String id, RAMDTO ramDTO);

    Optional<RAMDTO> partialUpdate(String id, RAMDTO ramDTO);

    void delete(String id);
}