package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartNVMEScrapingService;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static com.price_tracker.testing_data.nvme_data.NVMETestingData.TESTING_NVME_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_CRUCIAL_P510_1TB;

public class NVMEUnitIntegrationTests {

    private final UmartNVMEScrapingService scraper = new UmartNVMEScrapingService(new PricePointObserver());

    @Test
    public void testThatUmartNVMEScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_CRUCIAL_P510_1TB);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_NVME_MODEL_NUMBER);
    }
}