package com.price_tracker.webscraper;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.domain.entities.RAMPricePoint;
import com.price_tracker.repositories.GPUPricePointRepository;
import com.price_tracker.repositories.RAMPricePointRepository;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.webscraper.product_services.impl.UmartGPUScraper;
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
public class GPUScrapingService {

    private final RAMPricePointRepository ramPricePointRepository;
    private final GPUPricePointRepository gpuPricePointRepository;
    private final UmartProductRepository umartProductRepository;
    private final UmartGPUScraper umartGPUScraper;
    private final UmartRAMScraper umartRAMScraper;

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
            Thread.sleep(SLEEPING_CONSTANT);
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

    public void runDailyRAMScrape() throws InterruptedException {
        runUmartRAMScrape();
    }

    public void runUmartRAMScrape() throws InterruptedException {
        for(String url : umartProductRepository.findUrlsForActiveRAM()) {
            RAMPricePoint ramPricePoint = scrapeRAM(url);
            Thread.sleep(SLEEPING_CONSTANT);
            if(ramPricePoint != null) {
                log.info("Got RAM Model: " + ramPricePoint.getModelNumber() + " & RAM  Price: "
                        + ramPricePoint.getPrice());
                ramPricePointRepository.save(ramPricePoint);
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
