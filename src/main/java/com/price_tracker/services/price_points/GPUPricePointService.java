package com.price_tracker.services.price_points;

import com.price_tracker.domain.dto.GPUPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface GPUPricePointService {

    List<GPUPricePointDTO> findAll();

    Optional<GPUPricePointDTO> findOne(Long id);
}
