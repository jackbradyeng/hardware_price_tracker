package com.price_tracker.webscraper;

import com.price_tracker.domain.entities.RAMPricePoint;
import com.price_tracker.repositories.RAMPricePointRepository;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartRAMScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import static com.price_tracker.constants.ScrapingConstants.SLEEPING_CONSTANT;

@Service
@RequiredArgsConstructor
@Log
public class RAMScrapingService {

    private final RAMPricePointRepository ramPricePointRepository;
    private final UmartProductRepository umartProductRepository;
    private final UmartRAMScraper umartRAMScraper;

    // @Scheduled(cron = "0 00 22 * * ?")
    public void runDailyScrape() throws InterruptedException {
        log.info("Scraping service started at " + LocalDateTime.now());
        runDailyRAMScrape();
        log.info("Scraping service completed at " + LocalDateTime.now());
    }

    public void runDailyRAMScrape() throws InterruptedException {
        runUmartRAMScrape();
    }

    public void runUmartRAMScrape() throws InterruptedException {
        for(String url : umartProductRepository.findUrlsForActiveRAM()) {
            RAMPricePoint ramPricePoint = scrapeRAM(url);
            if(ramPricePoint != null) {
                Thread.sleep(SLEEPING_CONSTANT);
                log.info("Got RAM Model: " + ramPricePoint.getModelNumber() + " & RAM  Price: "
                        + ramPricePoint.getPrice());
                ramPricePointRepository.save(ramPricePoint);
            } else {
                log.info("Could not create RAM price point entity.");
            }
        }
    }

    public RAMPricePoint scrapeRAM(String url) {
        String[] scrapedData = umartRAMScraper.scrapeProductData(url);
        if(umartRAMScraper.validateScrapedData(scrapedData))
            return umartRAMScraper.createRAMPricePoint(scrapedData);
        else
            return null;
    }
}
