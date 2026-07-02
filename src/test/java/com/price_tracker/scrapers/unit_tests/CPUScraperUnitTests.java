package com.price_tracker.scrapers.unit_tests;

import com.price_tracker.webscraper.PricePointObserver;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.VendorCPUScrapingService;
import com.price_tracker.webscraper.vendor_templates.UmartProductScraper;
import com.price_tracker.webscraper.vendor_templates.ScorptecProductScraper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.*;
import static com.price_tracker.testing_data.cpu_data.CPUTestingData.*;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.SCORPTEC_RYZEN_5_9600X;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_RYZEN_9_9600X;

public class CPUScraperUnitTests {

    private final PricePointObserver pricePointObserver = new PricePointObserver();
    private final UmartProductScraper umartProductScraper = new UmartProductScraper(pricePointObserver);
    private final ScorptecProductScraper scorptecProductScraper = new ScorptecProductScraper(pricePointObserver);
    private final VendorCPUScrapingService umartScraper = new VendorCPUScrapingService(umartProductScraper);
    private final VendorCPUScrapingService scorptecScraper = new VendorCPUScrapingService(scorptecProductScraper);

    @Test
    public void testThatUmartCPUScrapingServiceReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = umartScraper
                .getGenericVendorScraper()
                .scrapeProductData(UMART_RYZEN_9_9600X,UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION);

        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_CPU_MODEL_NUMBER);
    }

    @Test
    public void testThatScorptecCPUScrapingServiceReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scorptecScraper
                .getGenericVendorScraper()
                .scrapeProductData(SCORPTEC_RYZEN_5_9600X, SCORPTEC_CSS_MODEL_LOCATION, SCORPTEC_CSS_PRICE_LOCATION);

        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_CPU_MODEL_NUMBER);
    }

    @Test
    public void testThatUmartCPUScrapingServiceRemovesSemicolon() {
        String refinedModelNumber = umartProductScraper
                .refineModelNumber("Model Number : 100-100001405WOF");
        assert refinedModelNumber.equals("100-100001405WOF");
    }

    @Test
    public void testThatUmartCPUScrapingServiceRemovesSingleComma() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,444.00");
        assert refinedPrice.equals(new BigDecimal("1444.00"));
    }

    @Test
    public void testThatUmartCPUScrapingServiceRemovesMultipleCommas() {
        BigDecimal refinedPrice = umartProductScraper.refinePrice("1,240,000.00");
        assert  refinedPrice.equals(new BigDecimal("1240000.00"));
    }
}
