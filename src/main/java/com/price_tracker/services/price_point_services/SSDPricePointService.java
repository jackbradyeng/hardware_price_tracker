package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.SSDDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.SSDPricePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface SSDPricePointService {

    Page<SSDPricePointDTO> findAll(Pageable pageable);

    Optional<SSDDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable);
}