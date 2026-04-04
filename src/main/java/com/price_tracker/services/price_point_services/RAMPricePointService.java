package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.RAMPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface RAMPricePointService {

    List<RAMPricePointDTO> findAll();

    Optional<RAMDataAndPricePointDTO> findByModelNumber(String modelNumber);
}
