package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartNVMEScrapingService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;

import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.testing_data.nvme_data.NVMETestingData.TESTING_NVME_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_CRUCIAL_P510_1TB;

public class NVMEScraperUnitTests {

    private final UmartNVMEScrapingService scraper = new UmartNVMEScrapingService(new PricePointObserver());

    @Test
    public void testThatUmartNVMEScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper
                .scrapeProductData(UMART_CRUCIAL_P510_1TB, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_NVME_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartNVMEScraperRemovesSemicolon() {
        String refinedModelNumber = scraper.refineModelNumber("Model Number : " + TESTING_NVME_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_NVME_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartNVMEScraperRemovesSingleComma() {
        BigDecimal refinedPrice = scraper.refinePrice("2,999.00");
        assert refinedPrice.equals(new BigDecimal("2999.00"));
    }

    @Test
    public void testThatUmartNVMEScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = scraper.refinePrice("1,999,000.00");
        assert refinedPrice.equals(new BigDecimal("1999000.00"));
    }
}