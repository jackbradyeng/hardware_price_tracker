package com.price_tracker.webscraper.orchestrators.umart_orchestrators;

import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.RAMPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.VendorRAMScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.other_constants.ScrapingConstants.*;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;

@Log
@Service
@RequiredArgsConstructor
public class UmartRAMScrapingOrchestrator {

    private final RAMPricePointJDBCTemplate ramPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final VendorRAMScrapingService vendorRAMScrapingService;

    /** Core scraping service. Runs automatically each day as per the CRON notation below. */
    @Scheduled(cron = RAM_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartRAMScrape();
    }

    @SneakyThrows
    private void runUmartRAMScrape() {
        Instant start = Instant.now();

        List<RAMPricePoint> pricePoints = umartProductRepository.findUrlsForActiveRAM()
                .stream()
                .map(this::processRAM)
                .flatMap(Optional::stream)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("RAM scraping service took %d seconds to execute.".formatted(timeElapsed.toSeconds()));
    }

    private Optional<RAMPricePoint> processRAM(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return vendorRAMScrapingService
                    .getGenericVendorScraper()
                    .scrapeProductData(url, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION)
                    .map(vendorRAMScrapingService::createRAMPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
