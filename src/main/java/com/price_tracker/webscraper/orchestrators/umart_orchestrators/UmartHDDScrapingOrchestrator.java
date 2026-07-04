package com.price_tracker.webscraper.orchestrators.umart_orchestrators;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.HDDPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.HDDPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.webscraper.orchestrators.GenericProductScrapingOrchestrator;
import com.price_tracker.webscraper.product_services.impl.VendorProductScrapingService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static com.price_tracker.constants.other_constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.other_constants.ScrapingConstants.UMART_HDD_SCRAPING_TIME;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;

@Log
@Service
public class UmartHDDScrapingOrchestrator implements GenericProductScrapingOrchestrator {

    private final HDDPricePointJDBCTemplate hddPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final VendorProductScrapingService vendorProductScrapingService;
    private final GenericMapper<HDDPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public UmartHDDScrapingOrchestrator(HDDPricePointJDBCTemplate hddPricePointJDBCTemplate,
                                        UmartProductRepository umartProductRepository,
                                        VendorProductScrapingService vendorProductScrapingService,
                                        MapperFactory mapperFactory) {
        this.hddPricePointJDBCTemplate = hddPricePointJDBCTemplate;
        this.umartProductRepository = umartProductRepository;
        this.vendorProductScrapingService = vendorProductScrapingService;
        this.pricePointMapper = mapperFactory.create(HDDPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = UMART_HDD_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartHDDScrape();
    }

    private void runUmartHDDScrape() {
        Instant start = Instant.now();

        List<HDDPricePoint> pricePoints = umartProductRepository.findUrlsForActiveHDDs()
                .stream()
                .map(url -> processPricePoint(vendorProductScrapingService, url, UMART_CSS_MODEL_LOCATION,
                        UMART_CSS_PRICE_LOCATION, UMART, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        hddPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s HDD scraping service took %d seconds to execute.".formatted(UMART, timeElapsed.toSeconds()));
    }
}