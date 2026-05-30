package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.hybrid_dtos.NVMEDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.NVMEPricePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface NVMEPricePointService {

    Page<NVMEPricePointDTO> findAll(Pageable pageable);

    Optional<NVMEDataAndPricePointDTO> findByModelNumber(String modelNumber, Pageable pageable);
}