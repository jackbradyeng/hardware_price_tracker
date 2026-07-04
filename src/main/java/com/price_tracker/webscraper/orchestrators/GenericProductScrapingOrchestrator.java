package com.price_tracker.webscraper.orchestrators;

import java.util.Optional;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.webscraper.product_services.impl.VendorProductScrapingService;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SLEEPING_CONSTANT;

public interface GenericProductScrapingOrchestrator {

    default Optional<GenericPricePointDTO> processPricePoint(VendorProductScrapingService vendorProductScrapingService,
                                                             String url, String modelNumberLocation,
                                                             String priceLocation, String vendor, String currency) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return vendorProductScrapingService
                    .getGenericVendorScraper()
                    .scrapeProductData(url, modelNumberLocation, priceLocation)
                    .map(scrapedData -> vendorProductScrapingService
                            .createGenericPricePoint(scrapedData, vendor, currency));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }
}
