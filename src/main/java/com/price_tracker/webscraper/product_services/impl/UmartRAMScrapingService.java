package com.price_tracker.webscraper.product_services.impl;

import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.vendor_templates.GenericVendorScraper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import static com.price_tracker.constants.other_constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;

@Log
@Service
@AllArgsConstructor
public class UmartRAMScrapingService {

    @Getter private GenericVendorScraper genericVendorScraper;

    public RAMPricePoint createRAMPricePoint(ScrapedDataDTO scrapedData) {

        return RAMPricePoint.builder()
                .modelNumber(scrapedData.modelNumber())
                .vendor(UMART)
                .currency(AUD)
                .price(scrapedData.price())
                .scrapedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .build();
    }
}
