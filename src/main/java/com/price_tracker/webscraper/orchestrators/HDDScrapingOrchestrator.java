package com.price_tracker.webscraper.orchestrators;

import com.price_tracker.domain.entities.price_point_entities.HDDPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.HDDPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartHDDScrapingService;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
@Log
public class HDDScrapingOrchestrator {

    private final HDDPricePointJDBCTemplate hddPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final UmartHDDScrapingService umartHDDScrapingService;

    @Scheduled(cron = HDD_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartHDDScrape();
    }

    private void runUmartHDDScrape() {
        Instant start = Instant.now();

        List<HDDPricePoint> pricePoints = umartProductRepository.findUrlsForActiveHDDs()
                .stream()
                .map(this::processHDD)
                .flatMap(Optional::stream)
                .toList();

        hddPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("HDD scraping service took %d seconds to execute.".formatted(timeElapsed.toSeconds()));
    }

    private Optional<HDDPricePoint> processHDD(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return umartHDDScrapingService
                    .getGenericVendorScraper()
                    .scrapeProductData(url, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION)
                    .map(umartHDDScrapingService::createHDDPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
