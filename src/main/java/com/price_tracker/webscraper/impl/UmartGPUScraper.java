package com.price_tracker.webscraper.impl;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.webscraper.GPUScraper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static com.price_tracker.constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.UmartCSSLocations.*;
import static com.price_tracker.constants.VendorConstants.UMART;

@Data
@NoArgsConstructor
public class UmartGPUScraper implements GPUScraper {

    @Override
    public String[] scrapeGPUData(String url) {

        // an array of size two containing the model number and price only
        String[] scrapedValues = new String[2];

        try {
            Document document = Jsoup.connect(url).get();
            String rawModelNumber = document.select(UMART_CSS_MODEL_LOCATION).text();
            String price = document.select(UMART_CSS_PRICE_LOCATION).text();

            // if the return element has "Model Number: ", remove it.
            String modelNumber = rawModelNumber.contains(":") ? rawModelNumber.split(":")[1].trim() : rawModelNumber;

            scrapedValues[0] = modelNumber;
            scrapedValues[1] = price;
            logModelNumberAndPrice(modelNumber, price, url);
            return scrapedValues;
        } catch (IOException e) {
            // need more robust logging
            e.printStackTrace();
            return null;
        }
    }

    /** Logs the scraped model number and price given a particular URL. */
    public void logModelNumberAndPrice(String modelNumber, String price, String url) {
        System.out.println("========================================");
        System.out.println("UMART - GPUs - FROM: " + url);
        System.out.println(modelNumber);
        System.out.println(price);
        System.out.println("========================================");
    }

    /** Verifies that the scraped data is not blank and can thus be saved to the DB. */
    @Override
    public boolean validateScrapedData(String[] scrapedValues) {
        try {
            return (!scrapedValues[0].isBlank() && !scrapedValues[1].isBlank());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Scraped values array is empty.");
            return false;
        }
    }

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
