package com.price_tracker.webscraper.orchestrators.scorptec_orchestrators;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.SSDPricePointJDBCTemplate;
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
import static com.price_tracker.constants.other_constants.ScrapingConstants.SCORPTEC_SSD_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SCORPTEC_SLEEPING_CONSTANT;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.*;
import static com.price_tracker.constants.vendor_constants.VendorNames.SCORPTEC;

@Log
@Service
public class ScorptecSSDScrapingOrchestrator implements GenericProductScrapingOrchestrator {

    private final SSDPricePointJDBCTemplate ssdPricePointJDBCTemplate;
    private final ScorptecProductRepository scorptecProductRepository;
    private final VendorProductScrapingService vendorProductScrapingService;
    private final GenericVendorScraper scorptecProductScraper;
    private final GenericMapper<SSDPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public ScorptecSSDScrapingOrchestrator(SSDPricePointJDBCTemplate ssdPricePointJDBCTemplate,
                                           ScorptecProductRepository scorptecProductRepository,
                                           VendorProductScrapingService vendorProductScrapingService,
                                           @Qualifier("scorptecProductScraper") GenericVendorScraper scorptecProductScraper,
                                           MapperFactory mapperFactory) {
        this.ssdPricePointJDBCTemplate = ssdPricePointJDBCTemplate;
        this.scorptecProductRepository = scorptecProductRepository;
        this.vendorProductScrapingService = vendorProductScrapingService;
        this.scorptecProductScraper = scorptecProductScraper;
        this.pricePointMapper = mapperFactory.create(SSDPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = SCORPTEC_SSD_SCRAPING_TIME)
    public void runDailyScrape() { runScorptecSSDScrape(); }

    public void runScorptecSSDScrape() {
        Instant start = Instant.now();

        List<SSDPricePoint> pricePoints = scorptecProductRepository.findUrlsForActiveSSDs()
                .stream()
                .map(url -> processPricePoint(scorptecProductScraper, vendorProductScrapingService, SCORPTEC_SLEEPING_CONSTANT,
                        url, SCORPTEC_CSS_MODEL_LOCATION, SCORPTEC_CSS_PRICE_LOCATION, SCORPTEC, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        ssdPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s SSD scraping service took %d seconds to execute.".formatted(SCORPTEC, timeElapsed.toSeconds()));
    }
}