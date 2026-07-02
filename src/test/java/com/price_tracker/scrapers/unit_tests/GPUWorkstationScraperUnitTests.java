package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.VendorGPUWorkstationScrapingService;
import com.price_tracker.webscraper.vendor_templates.GenericUmartScraper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_RTX_PRO_6000;
import static com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingData.TESTING_WS_GPU_MODEL_NUMBER;

public class GPUWorkstationScraperUnitTests {

    private final GenericUmartScraper genericUmartScraper = new GenericUmartScraper(new PricePointObserver());
    private final VendorGPUWorkstationScrapingService vendorScraper = new VendorGPUWorkstationScrapingService(genericUmartScraper);

    @Test
    public void testThatUmartWSGPUScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = vendorScraper
                .getGenericVendorScraper()
                .scrapeProductData(UMART_RTX_PRO_6000, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_WS_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartWSGPUScraperRemovesSemicolon() {
        String refinedModelNumber = genericUmartScraper.refineModelNumber("Model Number : " + TESTING_WS_GPU_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_WS_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartWSGPUScraperRemovesSingleComma() {
        BigDecimal refinedPrice = genericUmartScraper.refinePrice("9,990.00");
        assert refinedPrice.equals(new BigDecimal("9990.00"));
    }

    @Test
    public void testThatUmartWSGPUScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = genericUmartScraper.refinePrice("1,099,000.00");
        assert refinedPrice.equals(new BigDecimal("1099000.00"));
    }
}