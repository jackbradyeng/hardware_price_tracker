package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface GPUPricePointService {

    List<GPUPricePointDTO> findAll();

    List<GPUDataAndPricePointDTO> findByModelNumber(String modelNumber);

    Optional<GPUPricePointDTO> findOne(Long id);
}
