package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.CPUPricePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CPUPricePointService {

    Page<CPUPricePointDTO> findAll(Pageable pageable);

    Optional<CPUDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable);
}
