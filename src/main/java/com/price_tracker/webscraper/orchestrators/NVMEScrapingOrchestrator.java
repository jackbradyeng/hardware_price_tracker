package com.price_tracker.webscraper.orchestrators;

import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.NVMEPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartNVMEScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.other_constants.ScrapingConstants.NVME_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SLEEPING_CONSTANT;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;

@Service
@RequiredArgsConstructor
@Log
public class NVMEScrapingOrchestrator {

    private final NVMEPricePointJDBCTemplate nvmePricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final UmartNVMEScrapingService umartNVMEScrapingService;

    @Scheduled(cron = NVME_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartNVMEScrape();
    }

    private void runUmartNVMEScrape() {
        Instant start = Instant.now();

        List<NVMEPricePoint> pricePoints = umartProductRepository.findUrlsForActiveNVMEs()
                .stream()
                .map(this::processNVME)
                .flatMap(Optional::stream)
                .toList();

        nvmePricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("NVME scraping service took %d seconds to execute.".formatted(timeElapsed.toSeconds()));
    }

    private Optional<NVMEPricePoint> processNVME(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return umartNVMEScrapingService
                    .scrapeProductData(url, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION)
                    .map(umartNVMEScrapingService::createNVMEPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
