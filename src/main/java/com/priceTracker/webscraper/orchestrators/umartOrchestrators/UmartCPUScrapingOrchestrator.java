package com.priceTracker.webscraper.orchestrators.umartOrchestrators;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.entities.pricePointEntities.CPUPricePoint;
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
import static com.priceTracker.constants.otherConstants.ScrapingConstants.UMART_CPU_SCRAPING_TIME;
import static com.priceTracker.constants.otherConstants.ScrapingConstants.UMART_SLEEPING_CONSTANT;
import static com.priceTracker.constants.vendorConstants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.priceTracker.constants.vendorConstants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.priceTracker.constants.vendorConstants.VendorNames.UMART;

@Log
@Service
public class UmartCPUScrapingOrchestrator implements GenericScrapingOrchestrator {

    private final GenericPricePointJdbcTemplate<CPUPricePoint> cpuGenericPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final GenericScrapingService genericScrapingService;
    private final GenericVendorScraper umartProductScraper;
    private final GenericMapper<CPUPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public UmartCPUScrapingOrchestrator(GenericPricePointJdbcTemplate<CPUPricePoint> cpuGenericPricePointJDBCTemplate,
                                        UmartProductRepository umartProductRepository,
                                        GenericScrapingService genericScrapingService,
                                        @Qualifier("umartProductScraper") GenericVendorScraper umartProductScraper,
                                        MapperFactory mapperFactory) {
        this.cpuGenericPricePointJDBCTemplate = cpuGenericPricePointJDBCTemplate;
        this.umartProductRepository = umartProductRepository;
        this.genericScrapingService = genericScrapingService;
        this.umartProductScraper = umartProductScraper;
        this.pricePointMapper = mapperFactory.create(CPUPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = UMART_CPU_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartCPUScrape();
    }

    private void runUmartCPUScrape() {
        Instant start = Instant.now();

        List<CPUPricePoint> pricePoints = umartProductRepository.findUrlsForActiveCPU()
                .stream()
                .map(url -> processPricePoint(umartProductScraper, genericScrapingService, UMART_SLEEPING_CONSTANT,
                        url, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION, UMART, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        cpuGenericPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s CPU scraping service took %d seconds to execute.".formatted(UMART, timeElapsed.toSeconds()));
    }
}
