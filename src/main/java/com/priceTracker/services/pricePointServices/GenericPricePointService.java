package com.priceTracker.services.pricePointServices;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface GenericPricePointService<H> {

    Optional<List<GenericPricePointDTO>> saveAll(List<GenericPricePointDTO> pricePointDTOs);

    Page<GenericPricePointDTO> findAll(Pageable pageable);

    Optional<H> findByModelNumber(String modelNumber, Pageable pageable);
}