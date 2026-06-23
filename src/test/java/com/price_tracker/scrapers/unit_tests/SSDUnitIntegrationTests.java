package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartSSDScrapingService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static com.price_tracker.testing_data.ssd_data.SSDTestingData.TESTING_SSD_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_CRUCIAL_BX500_1TB;

public class SSDUnitIntegrationTests {

    private final UmartSSDScrapingService scraper = new UmartSSDScrapingService(new PricePointObserver());

    @Test
    @Disabled
    public void testThatUmartSSDScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_CRUCIAL_BX500_1TB);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_SSD_MODEL_NUMBER);
    }
}