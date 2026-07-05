package com.price_tracker.webscraper.orchestrators.scorptec_orchestrators;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GPUPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.ScorptecProductRepository;
import com.price_tracker.webscraper.orchestrators.GenericProductScrapingOrchestrator;
import com.price_tracker.webscraper.product_services.VendorProductScrapingService;
import com.price_tracker.webscraper.vendor_templates.GenericVendorScraper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.other_constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SCORPTEC_GPU_SCRAPING_TIME;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.*;
import static com.price_tracker.constants.vendor_constants.VendorNames.SCORPTEC;

@Log
@Service
public class ScorptecGPUScrapingOrchestrator implements GenericProductScrapingOrchestrator {

    private final GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate;
    private final ScorptecProductRepository scorptecProductRepository;
    private final VendorProductScrapingService vendorProductScrapingService;
    private final GenericVendorScraper scorptecProductScraper;
    private final GenericMapper<GPUPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public ScorptecGPUScrapingOrchestrator(GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate,
                                           ScorptecProductRepository scorptecProductRepository,
                                           VendorProductScrapingService vendorProductScrapingService,
                                           @Qualifier("scorptecProductScraper") GenericVendorScraper scorptecProductScraper,
                                           MapperFactory mapperFactory) {
        this.gpuPricePointJDBCTemplate = gpuPricePointJDBCTemplate;
        this.scorptecProductRepository = scorptecProductRepository;
        this.vendorProductScrapingService = vendorProductScrapingService;
        this.scorptecProductScraper = scorptecProductScraper;
        this.pricePointMapper = mapperFactory.create(GPUPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = SCORPTEC_GPU_SCRAPING_TIME)
    public void runDailyScrape() { runScorptecGPUScrape(); }

    public void runScorptecGPUScrape() {
        Instant start = Instant.now();

        List<GPUPricePoint> pricePoints = scorptecProductRepository.findUrlsForActiveGPUs()
                .stream()
                .map(url -> processPricePoint(scorptecProductScraper, vendorProductScrapingService, url,
                        SCORPTEC_CSS_MODEL_LOCATION, SCORPTEC_CSS_PRICE_LOCATION, SCORPTEC, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s GPU scraping service took %d seconds to execute.".formatted(SCORPTEC, timeElapsed.toSeconds()));
    }
}