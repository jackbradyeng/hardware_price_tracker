package com.price_tracker.services.price_points;

import com.price_tracker.domain.dto.GPUWorkstationPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface GPUWorkstationPricePointService {

    List<GPUWorkstationPricePointDTO> findAll();

    Optional<GPUWorkstationPricePointDTO> findOne(Long id);
}
