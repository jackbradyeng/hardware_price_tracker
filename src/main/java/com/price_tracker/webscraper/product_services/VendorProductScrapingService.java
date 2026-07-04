package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class VendorProductScrapingService {

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