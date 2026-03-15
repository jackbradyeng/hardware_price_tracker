package com.price_tracker.services.price_points;

import com.price_tracker.domain.dto.CPUPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface CPUPricePointService {

    List<CPUPricePointDTO> findAll();

    Optional<CPUPricePointDTO> findOne(Long id);
}
