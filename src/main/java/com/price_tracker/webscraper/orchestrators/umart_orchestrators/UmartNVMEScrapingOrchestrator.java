package com.price_tracker.webscraper.orchestrators.umart_orchestrators;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.NVMEPricePointJDBCTemplate;
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
import static com.price_tracker.constants.other_constants.ScrapingConstants.UMART_NVME_SCRAPING_TIME;
import static com.price_tracker.constants.other_constants.ScrapingConstants.UMART_SLEEPING_CONSTANT;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_MODEL_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorCSSLocations.UMART_CSS_PRICE_LOCATION;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;

@Log
@Service
public class UmartNVMEScrapingOrchestrator implements GenericProductScrapingOrchestrator {

    private final NVMEPricePointJDBCTemplate nvmePricePointJDBCTemplate;
    private final UmartProductRepository umartProductRepository;
    private final VendorProductScrapingService vendorProductScrapingService;
    private final GenericVendorScraper umartProductScraper;
    private final GenericMapper<NVMEPricePoint, GenericPricePointDTO> pricePointMapper;

    @Autowired
    public UmartNVMEScrapingOrchestrator(NVMEPricePointJDBCTemplate nvmePricePointJDBCTemplate,
                                         UmartProductRepository umartProductRepository,
                                         VendorProductScrapingService vendorProductScrapingService,
                                         @Qualifier("umartProductScraper") GenericVendorScraper umartProductScraper,
                                         MapperFactory mapperFactory) {
        this.nvmePricePointJDBCTemplate = nvmePricePointJDBCTemplate;
        this.umartProductRepository = umartProductRepository;
        this.vendorProductScrapingService = vendorProductScrapingService;
        this.umartProductScraper = umartProductScraper;
        this.pricePointMapper = mapperFactory.create(NVMEPricePoint.class, GenericPricePointDTO.class);
    }

    @Scheduled(cron = UMART_NVME_SCRAPING_TIME)
    public void runDailyScrape() {
        runUmartNVMEScrape();
    }

    private void runUmartNVMEScrape() {
        Instant start = Instant.now();

        List<NVMEPricePoint> pricePoints = umartProductRepository.findUrlsForActiveNVMEs()
                .stream()
                .map(url -> processPricePoint(umartProductScraper, vendorProductScrapingService, UMART_SLEEPING_CONSTANT,
                        url, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION, UMART, AUD))
                .flatMap(Optional::stream)
                .map(pricePointMapper::mapFrom)
                .toList();

        nvmePricePointJDBCTemplate.batchInsertPricePoints(pricePoints);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("%s NVME scraping service took %d seconds to execute.".formatted(UMART, timeElapsed.toSeconds()));
    }
}