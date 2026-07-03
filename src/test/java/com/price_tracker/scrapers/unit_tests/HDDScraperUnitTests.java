package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.VendorHDDScrapingService;
import com.price_tracker.webscraper.vendor_templates.UmartProductScraper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.testing_data.hdd_data.HDDTestingData.TESTING_HDD_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.VendorWebDomainNames.UMART_SEAGATE_ST2000DM005;

public class HDDScraperUnitTests {

    private final UmartProductScraper umartProductScraper = new UmartProductScraper(new PricePointObserver());
    private final VendorHDDScrapingService vendorScraper = new VendorHDDScrapingService(umartProductScraper);

    @Test
    public void testThatUmartHDDScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = vendorScraper
                .getGenericVendorScraper()
                .scrapeProductData(UMART_SEAGATE_ST2000DM005, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_HDD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartHDDScraperRemovesSemicolon() {
        String refinedModelNumber = umartProductScraper.refineModelNumber("Model Number : " + TESTING_HDD_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_HDD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartHDDScraperRemovesSingleComma() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,099.00");
        assert refinedPrice.equals(new BigDecimal("1099.00"));
    }

    @Test
    public void testThatUmartHDDScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,240,000.00");
        assert refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}