package com.price_tracker.domain.dto.hybrid_interfaces;

public interface GenericDataAndPricePointProjection<E, P> {

    E getEntity();
    P getPricePoint();
}