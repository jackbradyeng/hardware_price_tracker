package com.price_tracker.webscraper;

import com.price_tracker.domain.entities.RAMPricePoint;
import com.price_tracker.repositories.RAMPricePointRepository;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartRAMScraper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.ScrapingConstants.*;

@Service
@RequiredArgsConstructor
@Log
public class RAMScrapingService {

    private final RAMPricePointRepository ramPricePointRepository;
    private final UmartProductRepository umartProductRepository;
    private final UmartRAMScraper umartRAMScraper;

    /** Core scraping service. Runs automatically each day as per the CRON notation below. */
    @Scheduled(cron = RAM_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartRAMScrape();
    }

    @SneakyThrows
    private void runUmartRAMScrape() {
        List<RAMPricePoint> pricePoints = umartProductRepository.findUrlsForActiveRAM()
                .stream()
                .map(this::processRAM)
                .flatMap(Optional::stream)
                .toList();
        ramPricePointRepository.saveAll(pricePoints);
    }

    private Optional<RAMPricePoint> processRAM(String url) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return umartRAMScraper.scrapeProductData(url)
                    .map(umartRAMScraper::createRAMPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
