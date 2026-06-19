package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface GPUPricePointService {

    Page<GenericPricePointDTO> findAll(Pageable pageable);

    Optional<GPUDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable);
}
