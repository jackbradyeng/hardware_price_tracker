package com.price_tracker.domain.dto.hybrid_interfaces;

import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.domain.entities.product_entities.RAMEntity;

public interface RAMDataAndPricePointProjection {

    RAMPricePoint getRAMPricePoint();
    RAMEntity getRAMEntity();
}
