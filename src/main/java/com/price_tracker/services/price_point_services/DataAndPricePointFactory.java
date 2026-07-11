package com.price_tracker.services.price_point_services;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import java.util.List;

/** Builds a component's [Product]DataAndPricePointDTO from generic pieces. */
@FunctionalInterface
public interface DataAndPricePointFactory<D, H> {

    H create(D productDTO, List<GenericPricePointDTO> pricePointDTOList, int page, int pageSize,
             int totalPages, long totalElements);
}