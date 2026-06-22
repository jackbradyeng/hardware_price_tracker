package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartHDDScrapingService;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static com.price_tracker.testing_data.hdd_data.HDDTestingData.TESTING_HDD_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_SEAGATE_ST2000DM005;

public class HDDUnitIntegrationTests {

    private final UmartHDDScrapingService scraper = new UmartHDDScrapingService();

    @Test
    public void testThatUmartHDDScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_SEAGATE_ST2000DM005);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_HDD_MODEL_NUMBER);
    }
}