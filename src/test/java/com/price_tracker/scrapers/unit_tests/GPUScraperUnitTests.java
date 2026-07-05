package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.testing_data.fixture_capture.DocumentLoader;
import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.vendor_templates.impl.UmartProductScraper;
import com.price_tracker.webscraper.vendor_templates.impl.ScorptecProductScraper;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.*;
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.TESTING_GPU_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.VendorWebDomainNames.SCORPTEC_ASUS_5070TI;
import static com.price_tracker.testing_data.vendor_data.VendorWebDomainNames.UMART_ASUS_5070TI;

public class GPUScraperUnitTests {

    private final PricePointObserver pricePointObserver = new PricePointObserver();
    private final UmartProductScraper umartProductScraper = new UmartProductScraper(pricePointObserver);
    private final ScorptecProductScraper scorptecProductScraper = new ScorptecProductScraper(pricePointObserver);

    @Test
    public void testThatUmartGPUScraperReturnsExpectedModelNumber() {
        Document document = DocumentLoader.load("umart/asus_5070ti.html");
        Optional<ScrapedDataDTO> scrapedDataDTO = umartProductScraper
                .parseProductData(document, UMART_ASUS_5070TI, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatScorptecGPUScraperReturnsExpectedModelNumber() {
        Document document = DocumentLoader.load("scorptec/asus_5070ti.html");
        Optional<ScrapedDataDTO> scrapedDataDTO = scorptecProductScraper
                .parseProductData(document, SCORPTEC_ASUS_5070TI, SCORPTEC_CSS_MODEL_LOCATION, SCORPTEC_CSS_PRICE_LOCATION);
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