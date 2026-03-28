package com.price_tracker.domain.dto.hybrid_interfaces;

import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.domain.entities.product_entities.GPUEntity;

public interface GPUDataAndPricePointProjection {

    GPUPricePoint getGPUPricePoint();
    GPUEntity getGPUEntity();
}
