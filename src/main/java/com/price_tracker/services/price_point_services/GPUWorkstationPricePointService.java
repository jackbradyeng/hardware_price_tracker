package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.price_point_dtos.GPUWorkstationPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface GPUWorkstationPricePointService {

    List<GPUWorkstationPricePointDTO> findAll();

    Optional<GPUWorkstationPricePointDTO> findOne(Long id);
}
