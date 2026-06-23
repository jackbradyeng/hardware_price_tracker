package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartRAMScrapingService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.testing_data.ram_data.RAMTestingData.TESTING_RAM_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_KINGSTON_KINGSTON_F64G;

public class RAMScraperUnitTests {

    private final UmartRAMScrapingService scraper = new UmartRAMScrapingService(new PricePointObserver());

    @Test
    public void testThatUmartRAMScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_KINGSTON_KINGSTON_F64G);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_RAM_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartRAMScraperRemovesSemicolon() {
        String refinedModelNumber = scraper.refineModelNumber("Model Number : " + TESTING_RAM_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_RAM_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartRAMScraperRemovesSingleComma() {
        BigDecimal refinedPrice = scraper.refinePrice("1,890.00");
        assert refinedPrice.equals(new BigDecimal("1890.00"));
    }

    @Test
    public void testThatUmartRAMScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = scraper.refinePrice("1,240,000.00");
        assert refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}