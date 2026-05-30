package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.HDDDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.HDDPricePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface HDDPricePointService {

    Page<HDDPricePointDTO> findAll(Pageable pageable);

    Optional<HDDDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable);
}