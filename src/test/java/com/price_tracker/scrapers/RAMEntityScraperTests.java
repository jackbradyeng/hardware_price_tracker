package com.price_tracker.scrapers;

import com.price_tracker.webscraper.product_services.impl.UmartRAMScraper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static com.price_tracker.constants.TestingConstants.TESTING_RAM_MODEL_NUMBER;
import static com.price_tracker.constants.WebDomainNames.UMART_KINGSTON_KINGSTON_F64G;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RAMEntityScraperTests {

    private final UmartRAMScraper scraper;

    @Autowired
    public RAMEntityScraperTests(UmartRAMScraper scraper) { this.scraper = scraper; }

    @Test
    public void testTHatUmartRAMScraperReturnsExpectedModelNumber() {
        String[] scrapedData = scraper.scrapeProductData(UMART_KINGSTON_KINGSTON_F64G);
        assert  scrapedData[0].equals(TESTING_RAM_MODEL_NUMBER);
    }
}
