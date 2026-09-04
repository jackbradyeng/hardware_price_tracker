package com.priceTracker.testingData.scrapingJobData;

import com.priceTracker.domain.dto.scrapingJobDTOs.ScrapingJobResultDTO;
import com.priceTracker.domain.entities.scrapingJobEntities.ScrapingJobResult;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import static com.priceTracker.testingData.scrapingJobData.ScrapingJobTestingData.*;

@Component
public class ScrapingJobTestingUtility {

    /// SAMPLE ENTITIES
    public ScrapingJobResult createTestScrapingJobResult() {
        return createTestScrapingJobResult(TESTING_SCRAPING_JOB_VENDOR, TESTING_SCRAPING_JOB_PRODUCT_TYPE);
    }

    public ScrapingJobResult createTestScrapingJobResult(String vendor, String productType) {
        return ScrapingJobResult.builder()
                .vendor(vendor)
                .productType(productType)
                .recordsReceived(TESTING_SCRAPING_JOB_RECORDS_RECEIVED)
                .recordsSaved(TESTING_SCRAPING_JOB_RECORDS_SAVED)
                .timeCompleted(LocalDateTime.now())
                .build();
    }

    /// SAMPLE DTOS
    public ScrapingJobResultDTO createTestScrapingJobResultDTO() {
        return ScrapingJobResultDTO.builder()
                .vendor(TESTING_SCRAPING_JOB_VENDOR)
                .productType(TESTING_SCRAPING_JOB_PRODUCT_TYPE)
                .recordsReceived(TESTING_SCRAPING_JOB_RECORDS_RECEIVED)
                .recordsSaved(TESTING_SCRAPING_JOB_RECORDS_SAVED)
                .timeCompleted(LocalDateTime.now())
                .build();
    }
}