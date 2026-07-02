package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartHDDScrapingService;
import com.price_tracker.webscraper.vendor_templates.GenericUmartScraper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.testing_data.hdd_data.HDDTestingData.TESTING_HDD_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_SEAGATE_ST2000DM005;

public class HDDScraperUnitTests {

    private final GenericUmartScraper genericUmartScraper = new GenericUmartScraper(new PricePointObserver());
    private final UmartHDDScrapingService vendorScraper = new UmartHDDScrapingService(genericUmartScraper);

    @Test
    public void testThatUmartHDDScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = vendorScraper
                .getGenericVendorScraper()
                .scrapeProductData(UMART_SEAGATE_ST2000DM005, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_HDD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartHDDScraperRemovesSemicolon() {
        String refinedModelNumber = genericUmartScraper.refineModelNumber("Model Number : " + TESTING_HDD_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_HDD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartHDDScraperRemovesSingleComma() {
        BigDecimal refinedPrice = genericUmartScraper.refinePrice("1,099.00");
        assert refinedPrice.equals(new BigDecimal("1099.00"));
    }

    @Test
    public void testThatUmartHDDScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = genericUmartScraper.refinePrice("1,240,000.00");
        assert refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}