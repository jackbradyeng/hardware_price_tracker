package com.price_tracker.webscraper.vendor_templates;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import static com.price_tracker.constants.UmartCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.UmartCSSLocations.UMART_CSS_PRICE_LOCATION;

@Data
@Log
@RequiredArgsConstructor
public abstract class GenericUmartScraper {

    public String[] scrapeProductData(String url) {

        // an array of size two containing the model number and price only
        String[] scrapedValues = new String[2];

        try {
            Document document = Jsoup.connect(url).get();
            String rawModelNumber = document.select(UMART_CSS_MODEL_LOCATION).text();
            String rawPrice = document.select(UMART_CSS_PRICE_LOCATION).text();

            // remove all residual text from the model number.
            String modelNumber =
                    rawModelNumber.contains(":") ? rawModelNumber.split(":")[1].trim() : rawModelNumber;

            // remove any commas from the price tag
            String price = rawPrice.replaceAll(",", "");

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

    public boolean validateScrapedData(String[] scrapedValues) {
        try {
            return (!scrapedValues[0].isBlank() && !scrapedValues[1].isBlank());
        } catch (IndexOutOfBoundsException e) {
            log.info("Scraped values array is empty.");
            return false;
        }
    }

    /** Logs the scraped model number and price given a particular URL. */
    public void logModelNumberAndPrice(String modelNumber, String price, String url) {
        log.info("========================================");
        log.info("UMART - PRODUCT - FROM: " + url);
        log.info(modelNumber);
        log.info(price);
        log.info("========================================");
    }
}
