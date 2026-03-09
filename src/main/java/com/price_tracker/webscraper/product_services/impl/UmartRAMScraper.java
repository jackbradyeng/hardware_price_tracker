package com.price_tracker.webscraper.product_services.impl;

import com.price_tracker.domain.entities.RAMPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.RAMScraper;
import com.price_tracker.webscraper.vendor_templates.GenericUmartScraper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import static com.price_tracker.constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.VendorConstants.UMART;

@EqualsAndHashCode(callSuper = true)
@Data
@Service
@Log
public class UmartRAMScraper extends GenericUmartScraper implements RAMScraper {

    @Override
    public RAMPricePoint createRAMPricePoint(ScrapedDataDTO scrapedData) {

        return RAMPricePoint.builder()
                .modelNumber(scrapedData.modelNumber())
                .vendor(UMART)
                .currency(AUD)
                .price(scrapedData.price())
                .scrapedAt(LocalDateTime.now())
                .build();
    }
}
