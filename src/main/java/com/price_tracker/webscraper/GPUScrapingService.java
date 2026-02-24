package com.price_tracker.webscraper;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.repositories.GPUPricePointRepository;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartGPUScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log
public class GPUScrapingService {


    private final Integer sleepingConstant = 1000; // for polite scraping
    private final GPUPricePointRepository gpuPricePointRepository;
    private final UmartProductRepository umartProductRepository;
    private final UmartGPUScraper umartGPUScraper;

    /** Core scraping service. Runs automatically at 3AM each day as per the CRON notation below. */
    @Scheduled(cron = "0 0 3 * * ?")
    public void runDailyScrape() throws InterruptedException {
        System.out.println("Scraping service started at " + LocalDateTime.now());
        runDailyGPUScrape();
        System.out.println("Scraping service completed at " + LocalDateTime.now());
    }

    public void runDailyGPUScrape() throws InterruptedException {
        runUmartGPUScrape();
    }

    // both of these umart methods should be split off into a different class/interface and injected via the constructor
    public void runUmartGPUScrape() throws InterruptedException {
        for(String url : umartProductRepository.findUrlsForActiveGPUs()) {
            GPUPricePoint gpuPricePoint = scrapeGPU(url);
            Thread.sleep(sleepingConstant);
            if(gpuPricePoint != null) {
                log.info("Got GPU Model: " + gpuPricePoint.getModelNumber() + " & GPU Price: "
                        + gpuPricePoint.getPrice());
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
