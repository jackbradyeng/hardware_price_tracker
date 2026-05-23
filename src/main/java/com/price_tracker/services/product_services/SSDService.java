package com.price_tracker.services.product_services;

import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import java.util.List;
import java.util.Optional;

public interface SSDService {

    SSDDTO save(SSDDTO ssdDTO);

    List<SSDDTO> saveAll(List<SSDDTO> ssdDTOs);

    List<SSDDTO> findAll();

    Optional<SSDDTO> findOne(String id);

    Boolean exists(String id);

    Optional<SSDDTO> fullUpdate(String id, SSDDTO ssdDTO);

    Optional<SSDDTO> partialUpdate(String id, SSDDTO ssdDTO);

    void delete(String id);
}