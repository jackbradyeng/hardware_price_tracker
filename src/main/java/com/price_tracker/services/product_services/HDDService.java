package com.price_tracker.services.product_services;

import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import java.util.List;
import java.util.Optional;

public interface HDDService {

    HDDDTO save(HDDDTO hddDTO);

    List<HDDDTO> saveAll(List<HDDDTO> hddDTOs);

    List<HDDDTO> findAll();

    Optional<HDDDTO> findOne(String id);

    Boolean exists(String id);

    Optional<HDDDTO> fullUpdate(String id, HDDDTO hddDTO);

    Optional<HDDDTO> partialUpdate(String id, HDDDTO hddDTO);

    void delete(String id);
}