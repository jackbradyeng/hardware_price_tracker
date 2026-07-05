package com.price_tracker.webscraper.orchestrators.umart_orchestrators;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.CPUPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
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
import static com.price_tracker.constants.other_constants.ScrapingConstants.UMART_CPU_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.UMART_SLEEPING_CONSTANT;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;

@Log
@Service
public class UmartCPUScrapingOrchestrator implements GenericScrapingOrchestrator {

    private final CPUPricePointJDBCTemplate cpuPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final GenericScrapingService genericScrapingService;
    private final GenericVendorScraper umartProductScraper;
    private final GenericMapper<CPUPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public UmartCPUScrapingOrchestrator(CPUPricePointJDBCTemplate cpuPricePointJDBCTemplate,
                                        UmartProductRepository umartProductRepository,
                                        GenericScrapingService genericScrapingService,
                                        @Qualifier("umartProductScraper") GenericVendorScraper umartProductScraper,
                                        MapperFactory mapperFactory) {
        this.cpuPricePointJDBCTemplate = cpuPricePointJDBCTemplate;
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

        cpuPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s CPU scraping service took %d seconds to execute.".formatted(UMART, timeElapsed.toSeconds()));
    }
}
