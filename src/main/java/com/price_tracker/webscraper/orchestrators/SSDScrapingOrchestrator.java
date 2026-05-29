package com.price_tracker.webscraper.orchestrators;

import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.SSDPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartSSDScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SSD_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SLEEPING_CONSTANT;

@Service
@RequiredArgsConstructor
@Log
public class SSDScrapingOrchestrator {

    private final SSDPricePointJDBCTemplate ssdPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final UmartSSDScrapingService umartSSDScrapingService;

    @Scheduled(cron = SSD_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartSSDScrape();
    }

    private void runUmartSSDScrape() {
        Instant start = Instant.now();

        List<SSDPricePoint> pricePoints = umartProductRepository.findUrlsForActiveSSDs()
                .stream()
                .map(this::processSSD)
                .flatMap(Optional::stream)
                .toList();

        ssdPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("SSD scraping service took %d seconds to execute.".formatted(timeElapsed.toSeconds()));
    }

    private Optional<SSDPricePoint> processSSD(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return umartSSDScrapingService.scrapeProductData(url)
                    .map(umartSSDScrapingService::createSSDPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
