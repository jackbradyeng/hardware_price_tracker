package com.price_tracker.webscraper.product_services.impl;

import com.price_tracker.domain.entities.RAMPricePoint;
import com.price_tracker.webscraper.product_services.RAMScraper;
import com.price_tracker.webscraper.vendor_templates.GenericUmartScraper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.price_tracker.constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.VendorConstants.UMART;

@EqualsAndHashCode(callSuper = true)
@Data
@Service
@RequiredArgsConstructor
public class UmartRAMScraper extends GenericUmartScraper implements RAMScraper {

    @Override
    public RAMPricePoint createRAMPricePoint(String[] scrapedData) {

        try {
            String modelNumber = scrapedData[0];
            BigDecimal price = new BigDecimal(scrapedData[1]);
            LocalDateTime currentTime = LocalDateTime.now();

            return RAMPricePoint.builder()
                    .modelNumber(modelNumber)
                    .vendor(UMART)
                    .currency(AUD)
                    .price(price)
                    .scrapedAt(currentTime)
                    .build();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Scraped data array is empty.");
            return null;
        }
    }
}
