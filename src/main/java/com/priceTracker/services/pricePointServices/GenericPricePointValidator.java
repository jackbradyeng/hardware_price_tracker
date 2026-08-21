package com.priceTracker.services.pricePointServices;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import java.util.List;

/**
 * Validates a list of price point DTOs by ensuring all instances have the same vendor and product type.
 */
public interface GenericPricePointValidator {

    default boolean validatePricePointDTOs(List<GenericPricePointDTO> pricePointDTOs) {

        String vendor = pricePointDTOs.getFirst().getVendor();
        String productType = pricePointDTOs.getFirst().getProductType();

        for (GenericPricePointDTO pricePointDTO : pricePointDTOs) {
            if (!pricePointDTO.getVendor().equals(vendor))
                return false;

            if (!pricePointDTO.getProductType().equals(productType))
                return false;
        }

        return true;
    }
}