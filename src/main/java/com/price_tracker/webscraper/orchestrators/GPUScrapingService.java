package com.price_tracker.webscraper.orchestrators;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.repositories.GPUPricePointRepository;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartGPUScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.ScrapingConstants.GPU_SCRAPING_TIME;
import static com.price_tracker.constants.ScrapingConstants.SLEEPING_CONSTANT;

@Service
@RequiredArgsConstructor
@Log
public class GPUScrapingService {

    private final GPUPricePointRepository gpuPricePointRepository;
    private final UmartProductRepository umartProductRepository;
    private final UmartGPUScraper umartGPUScraper;

    /** Core scraping service. Runs automatically each day as per the CRON notation below. */
    @Scheduled(cron = GPU_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartGPUScrape();
    }

    private void runUmartGPUScrape() {
        /* This method converts the list of URL strings into a stream for modification. It then maps each string to the
        * process method below to obtain a list of PricePoint objects. It removes any Optionals from the stream and then
        * converts it back to a list. The reason for this is to avoid a stateful representation i.e. a functional
        * implementation bypasses the need for either the service or the scraper to maintain its own list. Additionally,
        * we avoid the N+1 problem by saving all PricePoints to the persistence layer at once via the saveAll call.*/
        Instant start = Instant.now();
        List<GPUPricePoint> pricePoints = umartProductRepository.findUrlsForActiveGPUs()
                .stream()
                .map(this::processGPU)
                .flatMap(Optional::stream)
                .toList();
        gpuPricePointRepository.saveAll(pricePoints);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("GPU scraping service took " + timeElapsed.toSeconds() + " seconds to execute.");
    }

    private Optional<GPUPricePoint> processGPU(String url) {
        /* We need to handle the thread interruption case in this method because the functional interface above does not
        * allow checked exceptions to be thrown. i.e. When processGPU is called from runUmartGPUScrape, a compile-time
        * error will be thrown unless processGPU handles the InterruptedException every time.*/
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return umartGPUScraper.scrapeProductData(url)
                    .map(umartGPUScraper::createGPUPricePoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Scraping interrupted for URL: " + url);
            return Optional.empty();
        }
    }
}
