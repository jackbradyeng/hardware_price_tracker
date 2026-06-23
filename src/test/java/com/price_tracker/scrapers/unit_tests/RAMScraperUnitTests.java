package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartRAMScrapingService;
import org.junit.jupiter.api.Test;
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
}