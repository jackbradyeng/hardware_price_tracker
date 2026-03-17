package com.price_tracker.webscraper.orchestrators;

import com.price_tracker.domain.entities.GPUWorkstationPricePoint;
import com.price_tracker.repositories.price_point_repos.GPUWorkstationPricePointRepository;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartGPUWorkstationScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.ScrapingConstants.GPU_WORKSTATION_SCRAPING_TIME;
import static com.price_tracker.constants.ScrapingConstants.SLEEPING_CONSTANT;

@Service
@RequiredArgsConstructor
@Log
public class GPUWorkstationScrapingOrchestrator {

    private final GPUWorkstationPricePointRepository gpuWorkstationPricePointRepository;
    private final UmartProductRepository umartProductRepository;
    private final UmartGPUWorkstationScrapingService umartGPUWorkstationScrapingService;

    @Scheduled(cron = GPU_WORKSTATION_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartWorkstationGPUScrape();
    }

    private void runUmartWorkstationGPUScrape() {
        Instant start = Instant.now();
        List<GPUWorkstationPricePoint> pricePoints = umartProductRepository.findUrlsForActiveWorkstationGPUs()
                .stream()
                .map(this::processWorkstationGPU)
                .flatMap(Optional::stream)
                .toList();
        gpuWorkstationPricePointRepository.saveAll(pricePoints);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("GPU workstation scraping service took " + timeElapsed.toSeconds() + " seconds to execute.");
    }

    private Optional<GPUWorkstationPricePoint> processWorkstationGPU(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return umartGPUWorkstationScrapingService.scrapeProductData(url)
                    .map(umartGPUWorkstationScrapingService::createGPUWorkstationPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
