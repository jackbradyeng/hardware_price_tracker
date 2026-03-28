package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import java.util.List;

public interface GPUPricePointService {

    List<GPUPricePointDTO> findAll();

    GPUDataAndPricePointDTO findByModelNumber(String modelNumber);
}
