package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.VendorProductScrapingService;
import com.price_tracker.webscraper.vendor_templates.UmartProductScraper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.testing_data.nvme_data.NVMETestingData.TESTING_NVME_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.VendorWebDomainNames.UMART_CRUCIAL_P510_1TB;

public class NVMEScraperUnitTests {

    private final UmartProductScraper umartProductScraper = new UmartProductScraper(new PricePointObserver());
    private final VendorProductScrapingService vendorScraper = new VendorProductScrapingService(umartProductScraper);

    @Test
    public void testThatUmartNVMEScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = vendorScraper
                .getGenericVendorScraper()
                .scrapeProductData(UMART_CRUCIAL_P510_1TB, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_NVME_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartNVMEScraperRemovesSemicolon() {
        String refinedModelNumber = umartProductScraper.refineModelNumber("Model Number : " + TESTING_NVME_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_NVME_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartNVMEScraperRemovesSingleComma() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("2,999.00");
        assert refinedPrice.equals(new BigDecimal("2999.00"));
    }

    @Test
    public void testThatUmartNVMEScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,999,000.00");
        assert refinedPrice.equals(new BigDecimal("1999000.00"));
    }
}