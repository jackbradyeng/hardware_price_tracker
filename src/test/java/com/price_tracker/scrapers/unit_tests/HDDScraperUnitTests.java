package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartHDDScrapingService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.testing_data.hdd_data.HDDTestingData.TESTING_HDD_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_SEAGATE_ST2000DM005;

public class HDDScraperUnitTests {

    private final UmartHDDScrapingService scraper = new UmartHDDScrapingService(new PricePointObserver());

    @Test
    public void testThatUmartHDDScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_SEAGATE_ST2000DM005);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_HDD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartHDDScraperRemovesSemicolon() {
        String refinedModelNumber = scraper.refineModelNumber("Model Number : " + TESTING_HDD_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_HDD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartHDDScraperRemovesSingleComma() {
        BigDecimal refinedPrice = scraper.refinePrice("1,099.00");
        assert refinedPrice.equals(new BigDecimal("1099.00"));
    }

    @Test
    public void testThatUmartHDDScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = scraper.refinePrice("1,240,000.00");
        assert refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}