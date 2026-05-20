package com.price_tracker.domain.dto.hybrid_interfaces;

import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.domain.entities.product_entities.SSDEntity;

public interface SSDDataAndPricePointProjection {

    SSDPricePoint getSSDPricePoint();
    SSDEntity getSSDEntity();
}