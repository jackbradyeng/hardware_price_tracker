package com.priceTracker.services.pricePointServices;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import java.util.List;

/** Builds a component's [Product]DataAndPricePointDTO from generic pieces. */
@FunctionalInterface
public interface DataAndPricePointFactory<D, H> {

    H create(D productDTO, List<GenericPricePointDTO> pricePointDTOList, int page, int pageSize,
             int totalPages, long totalElements);
}