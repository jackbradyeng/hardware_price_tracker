package com.price_tracker.webscraper.orchestrators;

import com.price_tracker.domain.entities.CPUPricePoint;
import com.price_tracker.repositories.CPUPricePointRepository;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartCPUScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.ScrapingConstants.CPU_SCRAPING_TIME;
import static com.price_tracker.constants.ScrapingConstants.SLEEPING_CONSTANT;

@Service
@RequiredArgsConstructor
@Log
public class CPUScrapingService {

    private final CPUPricePointRepository cpuPricePointRepository;
    private final UmartProductRepository umartProductRepository;
    private final UmartCPUScraper umartCPUScraper;

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
        cpuPricePointRepository.saveAll(pricePoints);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("CPU scraping service took " + timeElapsed.toSeconds() + " seconds to execute.");
    }

    private Optional<CPUPricePoint> processCPU(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return umartCPUScraper.scrapeProductData(url)
                    .map(umartCPUScraper::createCPUPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
