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
import static com.priceTracker.testingData.hddData.HDDTestingData.TESTING_HDD_MODEL_NUMBER;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_SEAGATE_ST2000DM005;

public class HDDScraperUnitTests {

    private final UmartProductScraper umartProductScraper = new UmartProductScraper(new PricePointObserver());

    @Test
    public void testThatUmartHDDScraperReturnsExpectedModelNumber() {
        Document document = DocumentLoader.load("umart/seagate_st2000dm005.html");
        Optional<ScrapedDataDTO> scrapedDataDTO = umartProductScraper
                .parseProductData(document, UMART_SEAGATE_ST2000DM005, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
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