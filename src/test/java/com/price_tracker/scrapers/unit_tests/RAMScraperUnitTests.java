package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartRAMScrapingService;
import com.price_tracker.webscraper.vendor_templates.GenericUmartScraper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.testing_data.ram_data.RAMTestingData.TESTING_RAM_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_KINGSTON_KINGSTON_F64G;

public class RAMScraperUnitTests {

    private final GenericUmartScraper genericUmartScraper = new GenericUmartScraper(new PricePointObserver());
    private final UmartRAMScrapingService vendorScraper = new UmartRAMScrapingService(genericUmartScraper);

    @Test
    public void testThatUmartRAMScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = vendorScraper
                .getGenericVendorScraper()
                .scrapeProductData(UMART_KINGSTON_KINGSTON_F64G, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_RAM_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartRAMScraperRemovesSemicolon() {
        String refinedModelNumber = genericUmartScraper.refineModelNumber("Model Number : " + TESTING_RAM_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_RAM_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartRAMScraperRemovesSingleComma() {
        BigDecimal refinedPrice = genericUmartScraper.refinePrice("1,890.00");
        assert refinedPrice.equals(new BigDecimal("1890.00"));
    }

    @Test
    public void testThatUmartRAMScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = genericUmartScraper.refinePrice("1,240,000.00");
        assert refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}