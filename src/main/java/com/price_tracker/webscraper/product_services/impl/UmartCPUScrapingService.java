package com.price_tracker.webscraper.product_services.impl;

import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.vendor_templates.GenericVendorScraper;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import static com.price_tracker.constants.other_constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;

@Log
@Service
public class UmartCPUScrapingService {

    @Getter private GenericVendorScraper genericVendorScraper;

    public CPUPricePoint createCPUPricePoint(ScrapedDataDTO scrapedData) {

        return CPUPricePoint.builder()
                .modelNumber(scrapedData.modelNumber())
                .vendor(UMART)
                .currency(AUD)
                .price(scrapedData.price())
                .scrapedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .build();
    }
}
