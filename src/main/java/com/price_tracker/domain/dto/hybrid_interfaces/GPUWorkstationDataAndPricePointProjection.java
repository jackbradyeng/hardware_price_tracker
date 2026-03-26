package com.price_tracker.domain.dto.hybrid_interfaces;

import com.price_tracker.domain.entities.price_point_entities.GPUWorkstationPricePoint;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;

public interface GPUWorkstationDataAndPricePointProjection {

    GPUWorkstationPricePoint getGPUWorkstationPricePoint();
    GPUWorkstationEntity getGPUWorkstationEntity();
}
