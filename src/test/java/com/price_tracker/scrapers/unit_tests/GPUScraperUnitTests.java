package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.VendorGPUScrapingService;
import com.price_tracker.webscraper.vendor_templates.UmartProductScraper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.TESTING_GPU_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_ASUS_5070TI;

public class GPUScraperUnitTests {

    private final UmartProductScraper umartProductScraper = new UmartProductScraper(new PricePointObserver());
    private final VendorGPUScrapingService vendorScraper = new VendorGPUScrapingService(umartProductScraper);

    @Test
    public void testThatUmartGPUScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = vendorScraper
                .getGenericVendorScraper()
                .scrapeProductData(UMART_ASUS_5070TI, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartGPUScraperRemovesSemicolon() {
        String refinedModelNumber = umartProductScraper
                .refineModelNumber("Model Number : " + TESTING_GPU_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartGPUScraperRemovesSingleComma() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,380.00");
        assert refinedPrice.equals(new BigDecimal("1380.00"));
    }

    @Test
    public void testThatUmartGPUScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,380,000.00");
        assert refinedPrice.equals(new BigDecimal("1380000.00"));
    }
}