package com.price_tracker.scrapers;

import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartGPUScraper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static com.price_tracker.constants.TestingConstants.TESTING_GPU_MODEL_NUMBER;
import static com.price_tracker.constants.WebDomainNames.UMART_ASUS_5070TI;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class GPUEntityScraperTests {

    private final UmartGPUScraper scraper;

    @Autowired
    public GPUEntityScraperTests(UmartGPUScraper scraper) {
        this.scraper = scraper;
    }

    @Test
    public void testThatUmartGPUScraperReturnsExpectedModelNumber() {

        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_ASUS_5070TI);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_GPU_MODEL_NUMBER);
    }
}
