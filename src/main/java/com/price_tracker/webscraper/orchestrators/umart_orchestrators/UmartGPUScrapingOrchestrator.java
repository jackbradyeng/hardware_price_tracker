package com.price_tracker.webscraper.orchestrators.umart_orchestrators;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GPUPricePointJDBCTemplate;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
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
import static com.price_tracker.constants.other_constants.ScrapingConstants.UMART_GPU_SCRAPING_TIME;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;

@Log
@Service
public class UmartGPUScrapingOrchestrator implements GenericProductScrapingOrchestrator {

    private final GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final VendorProductScrapingService vendorProductScrapingService;
    private final GenericVendorScraper umartProductScraper;
    private final GenericMapper<GPUPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public UmartGPUScrapingOrchestrator(GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate,
                                        UmartProductRepository umartProductRepository,
                                        VendorProductScrapingService vendorProductScrapingService,
                                        @Qualifier("umartProductScraper") GenericVendorScraper umartProductScraper,
                                        MapperFactory mapperFactory) {
        this.gpuPricePointJDBCTemplate = gpuPricePointJDBCTemplate;
        this.umartProductRepository = umartProductRepository;
        this.vendorProductScrapingService = vendorProductScrapingService;
        this.umartProductScraper = umartProductScraper;
        this.pricePointMapper = mapperFactory.create(GPUPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = UMART_GPU_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartGPUScrape();
    }

    private void runUmartGPUScrape() {
        Instant start = Instant.now();

        List<GPUPricePoint> pricePoints = umartProductRepository.findUrlsForActiveGPUs()
                .stream()
                .map(url -> processPricePoint(umartProductScraper, vendorProductScrapingService, url,
                        UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION, UMART, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s GPU scraping service took %d seconds to execute.".formatted(UMART, timeElapsed.toSeconds()));
    }
}