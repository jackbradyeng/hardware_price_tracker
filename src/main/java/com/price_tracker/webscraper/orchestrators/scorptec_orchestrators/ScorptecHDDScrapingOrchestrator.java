package com.price_tracker.webscraper.orchestrators.scorptec_orchestrators;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.HDDPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.HDDPricePointJDBCTemplate;
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
import static com.price_tracker.constants.other_constants.ScrapingConstants.SCORPTEC_HDD_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SCORPTEC_SLEEPING_CONSTANT;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.*;
import static com.price_tracker.constants.vendor_constants.VendorNames.SCORPTEC;

@Log
@Service
public class ScorptecHDDScrapingOrchestrator implements GenericProductScrapingOrchestrator {

    private final HDDPricePointJDBCTemplate hddPricePointJDBCTemplate;
    private final ScorptecProductRepository scorptecProductRepository;
    private final VendorProductScrapingService vendorProductScrapingService;
    private final GenericVendorScraper scorptecProductScraper;
    private final GenericMapper<HDDPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public ScorptecHDDScrapingOrchestrator(HDDPricePointJDBCTemplate hddPricePointJDBCTemplate,
                                           ScorptecProductRepository scorptecProductRepository,
                                           VendorProductScrapingService vendorProductScrapingService,
                                           @Qualifier("scorptecProductScraper") GenericVendorScraper scorptecProductScraper,
                                           MapperFactory mapperFactory) {
        this.hddPricePointJDBCTemplate = hddPricePointJDBCTemplate;
        this.scorptecProductRepository = scorptecProductRepository;
        this.vendorProductScrapingService = vendorProductScrapingService;
        this.scorptecProductScraper = scorptecProductScraper;
        this.pricePointMapper = mapperFactory.create(HDDPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = SCORPTEC_HDD_SCRAPING_TIME)
    public void runDailyScrape() { runScorptecHDDScrape(); }

    public void runScorptecHDDScrape() {
        Instant start = Instant.now();

        List<HDDPricePoint> pricePoints = scorptecProductRepository.findUrlsForActiveHDDs()
                .stream()
                .map(url -> processPricePoint(scorptecProductScraper, vendorProductScrapingService, SCORPTEC_SLEEPING_CONSTANT,
                        url, SCORPTEC_CSS_MODEL_LOCATION, SCORPTEC_CSS_PRICE_LOCATION, SCORPTEC, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        hddPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s HDD scraping service took %d seconds to execute.".formatted(SCORPTEC, timeElapsed.toSeconds()));
    }
}