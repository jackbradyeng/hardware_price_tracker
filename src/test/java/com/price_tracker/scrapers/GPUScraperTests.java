package com.price_tracker.scrapers;

import com.price_tracker.webscraper.GPUScraper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static com.price_tracker.constants.WebDomainNames.UMART_ASUS_5070TI;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GPUScraperTests {

    private final GPUScraper scraper;

    public GPUScraperTests() { this.scraper = new GPUScraper(); }

    @Test
    public void testThatUmartGPUScraperReturnsExpectedOutput() {
        assert scraper.scrapeGPUName(UMART_ASUS_5070TI).equals(
                "Asus Prime GeForce RTX 5070 Ti OC 16G Graphics Card (PRIME-RTX5070TI-O16G)");
    }
}
