package com.price_tracker.webscraper.orchestrators;

import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.CPUPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartCPUScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.other_constants.ScrapingConstants.CPU_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SLEEPING_CONSTANT;

@Service
@RequiredArgsConstructor
@Log
public class CPUScrapingOrchestrator {

    private final CPUPricePointJDBCTemplate cpuPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final UmartCPUScrapingService umartCPUScrapingService;

    @Scheduled(cron = CPU_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartCPUScrape();
    }

    private void runUmartCPUScrape() {
        Instant start = Instant.now();

        List<CPUPricePoint> pricePoints = umartProductRepository.findUrlsForActiveCPU()
                .stream()
                .map(this::processCPU)
                .flatMap(Optional::stream)
                .toList();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("CPU scraping service took %d seconds to execute.".formatted(timeElapsed.toSeconds()));
    }

    private Optional<CPUPricePoint> processCPU(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return umartCPUScrapingService.scrapeProductData(url)
                    .map(umartCPUScrapingService::createCPUPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
