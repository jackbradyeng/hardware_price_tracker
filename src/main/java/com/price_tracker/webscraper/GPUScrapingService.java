package com.price_tracker.webscraper;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.repositories.GPUPricePointRepository;
import com.price_tracker.repositories.GPURepository;
import com.price_tracker.webscraper.product_services.impl.UmartGPUScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Log
public class GPUScrapingService {

    private final GPURepository gpuRepository;
    private final GPUPricePointRepository gpuPricePointRepository;

    /* The problem here is that our dependencies will eventually become very difficult to maintain. What happens when
    we keep adding vendors and product types? What happens when a vendor goes bust, and we need to remove them
    from the service? What happens if we want to stop tracking a product type all-together? */

    /* What happens when a vendor's website goes down at the time of scraping? */
    private final UmartGPUScraper umartGPUScraper;

    /* These need to come from a DB table. It is unsustainable to log them in a constants class. */
    private final ArrayList<String> umartGPUURLs;

    /** Core scraping service. Runs automatically at 3AM each day as per the CRON notation. */
    @Scheduled(cron = "0 0 3 * * ?")
    public void runDailyScrape() {
        System.out.println("Scraping service started at " + LocalDateTime.now());
        runDailyGPUScrape();
        System.out.println("Scraping service completed at " + LocalDateTime.now());
    }

    public void runDailyGPUScrape() {
        runUmartGPUScrape();
    }

    // both of these umart methods should be split off into a different class/interface and injected via the constructor
    public void runUmartGPUScrape() {
        for(String url : umartGPUURLs) {
            GPUPricePoint gpuPricePoint = scrapeGPU(url);
            if(gpuPricePoint != null) {
                log.info("Got GPU Model & Price {}" + gpuPricePoint);
                gpuPricePointRepository.save(gpuPricePoint);
            }
        }
    }

    public GPUPricePoint scrapeGPU(String URL) {
        String[] scrapedData = umartGPUScraper.scrapeProductData(URL);
        if(umartGPUScraper.validateScrapedData(scrapedData))
            return umartGPUScraper.createGPUPricePoint(scrapedData);
        else
            return null;
    }
}
