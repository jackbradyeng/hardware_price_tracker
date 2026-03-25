package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.CPUPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface CPUPricePointService {

    List<CPUPricePointDTO> findAll();

    CPUDataAndPricePointDTO findByModelNumber(String modelNumber);
}
