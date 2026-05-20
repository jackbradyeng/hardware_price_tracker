package com.price_tracker.domain.dto.hybrid_interfaces;

import com.price_tracker.domain.entities.price_point_entities.HDDPricePoint;
import com.price_tracker.domain.entities.product_entities.HDDEntity;

public interface HDDDataAndPricePointProjection {

    HDDPricePoint getHDDPricePoint();
    HDDEntity getHDDEntity();
}