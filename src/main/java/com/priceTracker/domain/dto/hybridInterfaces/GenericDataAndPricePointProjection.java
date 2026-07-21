package com.priceTracker.domain.dto.hybridInterfaces;

public interface GenericDataAndPricePointProjection<E, P> {

    E getEntity();
    P getPricePoint();
}