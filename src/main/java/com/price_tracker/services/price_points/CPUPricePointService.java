package com.price_tracker.services.price_points;

import com.price_tracker.domain.dto.price_point_dtos.CPUPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface CPUPricePointService {

    List<CPUPricePointDTO> findAll();

    Optional<CPUPricePointDTO> findOne(Long id);
}
