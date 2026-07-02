package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartSSDScrapingService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;

import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.testing_data.ssd_data.SSDTestingData.TESTING_SSD_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_CRUCIAL_BX500_1TB;

public class SSDScraperUnitTests {

    private final UmartSSDScrapingService scraper = new UmartSSDScrapingService(new PricePointObserver());

    @Test
    public void testThatUmartSSDScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper
                .scrapeProductData(UMART_CRUCIAL_BX500_1TB, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_SSD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartSSDScraperRemovesSemicolon() {
        String refinedModelNumber = scraper.refineModelNumber("Model Number : " + TESTING_SSD_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_SSD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartSSDScraperRemovesSingleComma() {
        BigDecimal refinedPrice = scraper.refinePrice("1,199.00");
        assert refinedPrice.equals(new BigDecimal("1199.00"));
    }

    @Test
    public void testThatUmartSSDScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = scraper.refinePrice("1,240,000.00");
        assert refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}