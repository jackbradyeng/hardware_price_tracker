package com.price_tracker.webscraper.orchestrators.umart_orchestrators;

import com.price_tracker.domain.entities.price_point_entities.GPUWorkstationPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GPUWorkstationPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.VendorGPUWorkstationScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.other_constants.ScrapingConstants.UMART_GPU_WORKSTATION_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SLEEPING_CONSTANT;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;

@Log
@Service
@RequiredArgsConstructor
public class UmartGPUWorkstationScrapingOrchestrator {

    private final GPUWorkstationPricePointJDBCTemplate gpuWorkstationPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final VendorGPUWorkstationScrapingService vendorGPUWorkstationScrapingService;

    @Scheduled(cron = UMART_GPU_WORKSTATION_SCRAPING_TIME)
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

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("GPU workstation scraping service took %d seconds to execute.".formatted(timeElapsed.toSeconds()));
    }

    private Optional<GPUWorkstationPricePoint> processWorkstationGPU(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return vendorGPUWorkstationScrapingService
                    .getGenericVendorScraper()
                    .scrapeProductData(url, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION)
                    .map(vendorGPUWorkstationScrapingService::createGPUWorkstationPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
