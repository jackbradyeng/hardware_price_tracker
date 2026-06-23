package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartCPUScrapingService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.testing_data.cpu_data.CPUTestingData.*;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_RYZEN_9_9600X;

public class CPUUnitIntegrationTests {

    private final UmartCPUScrapingService scraper = new UmartCPUScrapingService(new PricePointObserver());

    @Test
    public void testThatUmartCPUScrapingServiceReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_RYZEN_9_9600X);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_CPU_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartCPUScrapingServiceRemovesSemicolon() {
        String refinedModelNumber = scraper.refineModelNumber("Model Number : 100-100001405WOF");
        assert refinedModelNumber.equals("100-100001405WOF");
    }

    @Test
    public void testThatUmartCPUScrapingServiceRemovesSingleComma() {
        BigDecimal refinedPrice = scraper.refinePrice("1,444.00");
        assert refinedPrice.equals(new BigDecimal("1444.00"));
    }

    @Test
    public void testThatUmartCPUScrapingServiceRemovesMultipleCommas() {
        BigDecimal refinedPrice = scraper.refinePrice("1,240,000.00");
        assert  refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}
