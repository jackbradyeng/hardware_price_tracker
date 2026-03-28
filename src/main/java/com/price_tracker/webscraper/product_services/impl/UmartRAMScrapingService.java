package com.price_tracker.webscraper.product_services.impl;

import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.RAMScraper;
import com.price_tracker.webscraper.vendor_templates.GenericUmartScraper;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.price_tracker.constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.VendorConstants.UMART;

@Service
@Log
public class UmartRAMScrapingService extends GenericUmartScraper implements RAMScraper {

    @Override
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
