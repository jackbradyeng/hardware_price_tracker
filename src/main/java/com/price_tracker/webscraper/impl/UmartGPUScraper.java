package com.price_tracker.webscraper.impl;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.webscraper.GPUScraper;
import com.price_tracker.webscraper.GenericUmartScraper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static com.price_tracker.constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.VendorConstants.UMART;

@Data
@Service
@RequiredArgsConstructor
public class UmartGPUScraper extends GenericUmartScraper implements GPUScraper {

    /** Returns a GPU Price Point instance using an array containing the model number and price (in that order) as
     * input. */
    @Override
    public GPUPricePoint createGPUPricePoint(String[] scrapedValues) {

        try {
            String modelNumber = scrapedValues[0];
            BigDecimal price = new BigDecimal(scrapedValues[1]);
            LocalDateTime currentTime = LocalDateTime.now();

            return GPUPricePoint.builder()
                    .modelNumber(modelNumber)
                    .vendor(UMART)
                    .currency(AUD)
                    .price(price)
                    .scrapedAt(currentTime)
                    .build();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Scraped values array is empty.");
            return null;
        }
    }
}
