package com.price_tracker.domain.dto.hybrid_interfaces;

import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import com.price_tracker.domain.entities.product_entities.CPUEntity;

public interface CPUDataANdPricePointProjection {

    CPUPricePoint getCPUPricePoint();
    CPUEntity getCPUEntity();
}
