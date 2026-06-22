package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartCPUScrapingService;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static com.price_tracker.testing_data.cpu_data.CPUTestingData.TESTING_CPU_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_RYZEN_9_9600X;

public class CPUUnitIntegrationTests {

    private final UmartCPUScrapingService scraper = new UmartCPUScrapingService();

    @Test
    public void testThatUmartCPUScrapingServiceReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_RYZEN_9_9600X);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_CPU_MODEL_NUMBER);
    }
}
