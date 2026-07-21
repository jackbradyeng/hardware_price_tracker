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
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_RTX_PRO_6000;
import static com.priceTracker.testingData.wsgpuData.WorkstationGPUTestingData.TESTING_WS_GPU_MODEL_NUMBER;

public class GPUWorkstationScraperUnitTests {

    private final UmartProductScraper umartProductScraper = new UmartProductScraper(new PricePointObserver());

    @Test
    public void testThatUmartWSGPUScraperReturnsExpectedModelNumber() {
        Document document = DocumentLoader.load("umart/rtx_pro_6000.html");
        Optional<ScrapedDataDTO> scrapedDataDTO = umartProductScraper
                .parseProductData(document, UMART_RTX_PRO_6000, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_WS_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartWSGPUScraperRemovesSemicolon() {
        String refinedModelNumber = umartProductScraper.refineModelNumber("Model Number : " + TESTING_WS_GPU_MODEL_NUMBER);
        assert refinedModelNumber.equals(TESTING_WS_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartWSGPUScraperRemovesSingleComma() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("9,990.00");
        assert refinedPrice.equals(new BigDecimal("9990.00"));
    }

    @Test
    public void testThatUmartWSGPUScraperRemovesMultipleCommas() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,099,000.00");
        assert refinedPrice.equals(new BigDecimal("1099000.00"));
    }
}