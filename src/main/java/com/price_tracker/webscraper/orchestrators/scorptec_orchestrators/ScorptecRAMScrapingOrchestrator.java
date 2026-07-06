package com.price_tracker.webscraper.orchestrators.scorptec_orchestrators;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GenericPricePointJdbcTemplate;
import com.price_tracker.repositories.vendor_repos.ScorptecProductRepository;
import com.price_tracker.webscraper.orchestrators.GenericScrapingOrchestrator;
import com.price_tracker.webscraper.product_services.GenericScrapingService;
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
import static com.price_tracker.constants.other_constants.ScrapingConstants.SCORPTEC_RAM_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SCORPTEC_SLEEPING_CONSTANT;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.*;
import static com.price_tracker.constants.vendor_constants.VendorNames.SCORPTEC;

@Log
@Service
public class ScorptecRAMScrapingOrchestrator implements GenericScrapingOrchestrator {

    private final GenericPricePointJdbcTemplate<RAMPricePoint> ramGenericPricePointJDBCTemplate;
    private final ScorptecProductRepository scorptecProductRepository;
    private final GenericScrapingService genericScrapingService;
    private final GenericVendorScraper scorptecProductScraper;
    private final GenericMapper<RAMPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public ScorptecRAMScrapingOrchestrator(GenericPricePointJdbcTemplate<RAMPricePoint> ramGenericPricePointJDBCTemplate,
                                           ScorptecProductRepository scorptecProductRepository,
                                           GenericScrapingService genericScrapingService,
                                           @Qualifier("scorptecProductScraper") GenericVendorScraper scorptecProductScraper,
                                           MapperFactory mapperFactory) {
        this.ramGenericPricePointJDBCTemplate = ramGenericPricePointJDBCTemplate;
        this.scorptecProductRepository = scorptecProductRepository;
        this.genericScrapingService = genericScrapingService;
        this.scorptecProductScraper = scorptecProductScraper;
        this.pricePointMapper = mapperFactory.create(RAMPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = SCORPTEC_RAM_SCRAPING_TIME)
    public void runDailyScrape() { runScorptecRAMScrape(); }

    public void runScorptecRAMScrape() {
        Instant start = Instant.now();

        List<RAMPricePoint> pricePoints = scorptecProductRepository.findUrlsForActiveRAM()
                .stream()
                .map(url -> processPricePoint(scorptecProductScraper, genericScrapingService, SCORPTEC_SLEEPING_CONSTANT,
                        url, SCORPTEC_CSS_MODEL_LOCATION, SCORPTEC_CSS_PRICE_LOCATION, SCORPTEC, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        ramGenericPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s RAM scraping service took %d seconds to execute.".formatted(SCORPTEC, timeElapsed.toSeconds()));
    }
}