package com.price_tracker.scrapers;

import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartRAMScraper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static com.price_tracker.constants.TestingConstants.TESTING_RAM_MODEL_NUMBER;
import static com.price_tracker.constants.WebDomainNames.UMART_KINGSTON_KINGSTON_F64G;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RAMEntityScraperTests {

    private final UmartRAMScraper scraper;

    @Autowired
    public RAMEntityScraperTests(UmartRAMScraper scraper) {
        this.scraper = scraper;
    }

    @Test
    public void testThatUmartRAMScraperReturnsExpectedModelNumber() {

        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_KINGSTON_KINGSTON_F64G);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_RAM_MODEL_NUMBER);
    }
}
