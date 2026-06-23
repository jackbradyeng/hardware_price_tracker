package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartGPUWorkstationScrapingService;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_RTX_PRO_6000;
import static com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingData.TESTING_WS_GPU_MODEL_NUMBER;

public class GPUWorkstationUnitIntegrationTests {

    private final UmartGPUWorkstationScrapingService scraper =
            new UmartGPUWorkstationScrapingService(new PricePointObserver());

    @Test
    public void testThatUmartWSGPUScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_RTX_PRO_6000);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_WS_GPU_MODEL_NUMBER);
    }
}