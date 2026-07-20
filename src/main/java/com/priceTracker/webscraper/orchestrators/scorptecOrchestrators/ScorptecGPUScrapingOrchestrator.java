package com.priceTracker.webscraper.orchestrators.scorptecOrchestrators;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.entities.pricePointEntities.GPUPricePoint;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.pricePointRepositories.jdbcTemplates.GenericPricePointJdbcTemplate;
import com.priceTracker.repositories.vendorRepositories.ScorptecProductRepository;
import com.priceTracker.webscraper.orchestrators.GenericScrapingOrchestrator;
import com.priceTracker.webscraper.productServices.GenericScrapingService;
import com.priceTracker.webscraper.vendorTemplates.GenericVendorScraper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.priceTracker.constants.otherConstants.CurrencyConstants.AUD;
import static com.priceTracker.constants.otherConstants.ScrapingConstants.SCORPTEC_GPU_SCRAPING_TIME;
import static com.priceTracker.constants.otherConstants.ScrapingConstants.SCORPTEC_SLEEPING_CONSTANT;
import static com.priceTracker.constants.vendorConstants.VendorCSSLocations.*;
import static com.priceTracker.constants.vendorConstants.VendorNames.SCORPTEC;

@Log
@Service
public class ScorptecGPUScrapingOrchestrator implements GenericScrapingOrchestrator {

    private final GenericPricePointJdbcTemplate<GPUPricePoint> gpuGenericPricePointJDBCTemplate;
    private final ScorptecProductRepository scorptecProductRepository;
    private final GenericScrapingService genericScrapingService;
    private final GenericVendorScraper scorptecProductScraper;
    private final GenericMapper<GPUPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public ScorptecGPUScrapingOrchestrator(GenericPricePointJdbcTemplate<GPUPricePoint> gpuGenericPricePointJDBCTemplate,
                                           ScorptecProductRepository scorptecProductRepository,
                                           GenericScrapingService genericScrapingService,
                                           @Qualifier("scorptecProductScraper") GenericVendorScraper scorptecProductScraper,
                                           MapperFactory mapperFactory) {
        this.gpuGenericPricePointJDBCTemplate = gpuGenericPricePointJDBCTemplate;
        this.scorptecProductRepository = scorptecProductRepository;
        this.genericScrapingService = genericScrapingService;
        this.scorptecProductScraper = scorptecProductScraper;
        this.pricePointMapper = mapperFactory.create(GPUPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = SCORPTEC_GPU_SCRAPING_TIME)
    public void runDailyScrape() { runScorptecGPUScrape(); }

    public void runScorptecGPUScrape() {
        Instant start = Instant.now();

        List<GPUPricePoint> pricePoints = scorptecProductRepository.findUrlsForActiveGPUs()
                .stream()
                .map(url -> processPricePoint(scorptecProductScraper, genericScrapingService, SCORPTEC_SLEEPING_CONSTANT,
                        url, SCORPTEC_CSS_MODEL_LOCATION, SCORPTEC_CSS_PRICE_LOCATION, SCORPTEC, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        gpuGenericPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s GPU scraping service took %d seconds to execute.".formatted(SCORPTEC, timeElapsed.toSeconds()));
    }
}