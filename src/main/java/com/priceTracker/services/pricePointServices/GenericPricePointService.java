package com.priceTracker.services.pricePointServices;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface GenericPricePointService<H> {

    Page<GenericPricePointDTO> findAll(Pageable pageable);

    Optional<H> findByModelNumber(String modelNumber, Pageable pageable);
}