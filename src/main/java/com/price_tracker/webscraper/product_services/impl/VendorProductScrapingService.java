package com.price_tracker.webscraper.product_services.impl;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.vendor_templates.GenericVendorScraper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Log
@Service
@AllArgsConstructor
public class VendorProductScrapingService {

    @Getter private GenericVendorScraper genericVendorScraper;

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
