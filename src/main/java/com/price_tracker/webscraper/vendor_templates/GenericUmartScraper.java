package com.price_tracker.webscraper.vendor_templates;

import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Level;
import static com.price_tracker.constants.vendor_constants.UmartCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.UmartCSSLocations.UMART_CSS_PRICE_LOCATION;

@Log
public class GenericUmartScraper {

    public Optional<ScrapedDataDTO> scrapeProductData(String url) {
        try {
            // connect to the url and extract raw HTML
            Document document = Jsoup.connect(url).get();
            String rawModelNumber = document.select(UMART_CSS_MODEL_LOCATION).text();
            String rawPrice = document.select(UMART_CSS_PRICE_LOCATION).text();

            // fail-fast if the document selection returns nothing
            if(rawModelNumber.isEmpty() || rawPrice.isEmpty()) {
                log.warning("WARNING: One or more fields returned no data.");
                logModelNumberAndPrice(rawModelNumber, rawPrice, url);
                return Optional.empty();
            }

            // refine scraped data
            String modelNumber = refineModelNumber(rawModelNumber);
            BigDecimal price = refinePrice(rawPrice);

            // log and return scraped data
            logModelNumberAndPrice(modelNumber, price.toString(), url);
            return Optional.ofNullable(ScrapedDataDTO.builder()
                    .modelNumber(modelNumber)
                    .price(price)
                    .build());
        } catch (IOException | NumberFormatException e) {
            log.log(Level.SEVERE, "WARNING: Failed to scrape " + url, e);
            return Optional.empty();
        }
    }

    /** Removes any header text and semicolons and returns the model number as a String. */
    public String refineModelNumber(String rawModelNumber) {
        return rawModelNumber.contains(":") ? rawModelNumber.split(":")[1].trim() : rawModelNumber;
    }

    /** Removes any trailing commas form the price tag and returns a BigDecimal object. */
    public BigDecimal refinePrice(String rawPrice) {
        return new BigDecimal(rawPrice.replace(",", ""));
    }

    /** Logs the scraped model number and price given a particular URL. */
    private void logModelNumberAndPrice(String modelNumber, String price, String url) {
        String logEntry = """
            
            ========================================
            UMART - PRODUCT - FROM: %s
            Model: %s
            Price: %s
            ========================================
            """.formatted(url, modelNumber, price);

        log.info(logEntry);
    }
}
