package com.priceTracker.scrapers.unitTests;

import com.priceTracker.testingData.documentCapture.DocumentLoader;
import com.priceTracker.webscraper.PricePointObserver;
import com.priceTracker.webscraper.dtos.ScrapedDataDTO;
import com.priceTracker.webscraper.vendorTemplates.impl.UmartProductScraper;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;

import static com.priceTracker.constants.vendorConstants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.priceTracker.constants.vendorConstants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.priceTracker.testingData.ssdData.SSDTestingData.TESTING_SSD_MODEL_NUMBER;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_CRUCIAL_BX500_1TB;

public class SSDScraperUnitTests {

    private final UmartProductScraper umartProductScraper = new UmartProductScraper(new PricePointObserver());

    @Test
    public void testThatUmartSSDScraperReturnsExpectedModelNumber() {
        Document document = DocumentLoader.load("umart/crucial_bx500_1tb.html");
        Optional<ScrapedDataDTO> scrapedDataDTO = umartProductScraper
                .parseProductData(document, UMART_CRUCIAL_BX500_1TB, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_SSD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartSSDScraperRemovesSemicolon() {
        String refinedModelNumber = umartProductScraper.refineModelNumber("Model Number : " + TESTING_SSD_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_SSD_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartSSDScraperRemovesSingleComma() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,199.00");
        assert refinedPrice.equals(new BigDecimal("1199.00"));
    }

    @Test
    public void testThatUmartSSDScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,240,000.00");
        assert refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}