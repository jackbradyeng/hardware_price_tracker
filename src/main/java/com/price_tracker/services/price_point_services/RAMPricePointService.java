package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.RAMPricePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RAMPricePointService {

    Page<RAMPricePointDTO> findAll(Pageable pageable);

    Optional<RAMDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable);
}
