package com.priceTracker.webscraper.orchestrators.umartOrchestrators;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.entities.pricePointEntities.RAMPricePoint;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.pricePointRepositories.jdbcTemplates.GenericPricePointJdbcTemplate;
import com.priceTracker.repositories.vendorRepositories.UmartProductRepository;
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
import static com.priceTracker.constants.otherConstants.ScrapingConstants.UMART_RAM_SCRAPING_TIME;
import static com.priceTracker.constants.otherConstants.ScrapingConstants.UMART_SLEEPING_CONSTANT;
import static com.priceTracker.constants.vendorConstants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.priceTracker.constants.vendorConstants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.priceTracker.constants.vendorConstants.VendorNames.UMART;

@Log
@Service
public class UmartRAMScrapingOrchestrator implements GenericScrapingOrchestrator {

    private final GenericPricePointJdbcTemplate<RAMPricePoint> ramGenericPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final GenericScrapingService genericScrapingService;
    private final GenericVendorScraper umartProductScraper;
    private final GenericMapper<RAMPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public UmartRAMScrapingOrchestrator(GenericPricePointJdbcTemplate<RAMPricePoint> ramGenericPricePointJDBCTemplate,
                                        UmartProductRepository umartProductRepository,
                                        GenericScrapingService genericScrapingService,
                                        @Qualifier("umartProductScraper") GenericVendorScraper umartProductScraper,
                                        MapperFactory mapperFactory) {
        this.ramGenericPricePointJDBCTemplate = ramGenericPricePointJDBCTemplate;
        this.umartProductRepository = umartProductRepository;
        this.genericScrapingService = genericScrapingService;
        this.umartProductScraper = umartProductScraper;
        this.pricePointMapper = mapperFactory.create(RAMPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = UMART_RAM_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartRAMScrape();
    }

    private void runUmartRAMScrape() {
        Instant start = Instant.now();

        List<RAMPricePoint> pricePoints = umartProductRepository.findUrlsForActiveRAM()
                .stream()
                .map(url -> processPricePoint(umartProductScraper, genericScrapingService, UMART_SLEEPING_CONSTANT,
                        url, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION, UMART, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        ramGenericPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s RAM scraping service took %d seconds to execute.".formatted(UMART, timeElapsed.toSeconds()));
    }
}