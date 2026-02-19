package com.price_tracker.scrapers;

import com.price_tracker.webscraper.product_services.impl.UmartGPUScraper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static com.price_tracker.constants.WebDomainNames.UMART_ASUS_5070TI;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GPUEntityScraperTests {

    private final UmartGPUScraper scraper;

    public GPUEntityScraperTests() { this.scraper = new UmartGPUScraper(); }

    @Test
    public void testThatUmartGPUScraperReturnsExpectedModelNumber() {
        String[] scrapedValues = scraper.scrapeProductData(UMART_ASUS_5070TI);
        assert scrapedValues[0].equals("PRIME-RTX5070TI-O16G");
    }
}
