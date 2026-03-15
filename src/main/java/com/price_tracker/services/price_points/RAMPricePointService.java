package com.price_tracker.services.price_points;

import com.price_tracker.domain.dto.RAMPricePointDTO;
import java.util.List;
import java.util.Optional;

public interface RAMPricePointService {

    List<RAMPricePointDTO> findAll();

    Optional<RAMPricePointDTO> findOne(Long id);
}
