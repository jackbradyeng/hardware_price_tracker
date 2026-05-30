package com.price_tracker.domain.dto.hybrid_interfaces;

import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;

public interface NVMEDataAndPricePointProjection {

    NVMEPricePoint getNVMEPricePoint();
    NVMEEntity getNVMEEntity();
}