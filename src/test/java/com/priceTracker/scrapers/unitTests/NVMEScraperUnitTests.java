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
import static com.priceTracker.testingData.nvmeData.NVMETestingData.TESTING_NVME_MODEL_NUMBER;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_CRUCIAL_P510_1TB;

public class NVMEScraperUnitTests {

    private final UmartProductScraper umartProductScraper = new UmartProductScraper(new PricePointObserver());

    @Test
    public void testThatUmartNVMEScraperReturnsExpectedModelNumber() {
        Document document = DocumentLoader.load("umart/crucial_p510_1tb.html");
        Optional<ScrapedDataDTO> scrapedDataDTO = umartProductScraper
                .parseProductData(document, UMART_CRUCIAL_P510_1TB, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
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