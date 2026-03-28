package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.GPUWorkstationDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GPUWorkstationPricePointDTO;
import java.util.List;

public interface GPUWorkstationPricePointService {

    List<GPUWorkstationPricePointDTO> findAll();

    GPUWorkstationDataAndPricePointDTO findByModelNumber(String modelNumber);
}
