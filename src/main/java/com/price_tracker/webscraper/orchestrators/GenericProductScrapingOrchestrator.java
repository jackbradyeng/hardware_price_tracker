package com.price_tracker.webscraper.orchestrators;

import java.util.Optional;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.webscraper.product_services.VendorProductScrapingService;
import com.price_tracker.webscraper.vendor_templates.GenericVendorScraper;
import static com.price_tracker.constants.other_constants.ScrapingConstants.SLEEPING_CONSTANT;

public interface GenericProductScrapingOrchestrator {

    default Optional<GenericPricePointDTO> processPricePoint(GenericVendorScraper genericVendorScraper,
                                                             VendorProductScrapingService vendorProductScrapingService,
                                                             String url, String modelNumberLocation,
                                                             String priceLocation, String vendor, String currency) {
        try {
            Thread.sleep(SLEEPING_CONSTANT);
            return genericVendorScraper
                    .scrapeProductData(url, modelNumberLocation, priceLocation)
                    .map(scrapedData -> vendorProductScrapingService
                            .createGenericPricePoint(scrapedData, vendor, currency));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }
}
