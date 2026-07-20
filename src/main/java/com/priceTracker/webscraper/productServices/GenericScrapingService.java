package com.priceTracker.webscraper.productServices;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * A generic service for generating GenericPricePointDTOs, with the ScrapedDataDTO, vendor, and currency fields passed
 * in as parameters.
 */
@Service
public class GenericScrapingService {

    public GenericPricePointDTO createGenericPricePoint(ScrapedDataDTO scrapedData, String vendor, String currency) {

        return GenericPricePointDTO.builder()
                .modelNumber(scrapedData.modelNumber())
                .vendor(vendor)
                .currency(currency)
                .price(scrapedData.price())
                .scrapedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .build();
    }
}