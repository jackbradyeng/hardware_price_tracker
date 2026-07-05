package com.price_tracker.webscraper.orchestrators;

import java.util.Optional;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.webscraper.product_services.GenericScrapingService;
import com.price_tracker.webscraper.vendor_templates.GenericVendorScraper;

public interface GenericProductScrapingOrchestrator {

    default Optional<GenericPricePointDTO> processPricePoint(GenericVendorScraper genericVendorScraper,
                                                             GenericScrapingService genericScrapingService,
                                                             Integer sleepingConstant,
                                                             String url,
                                                             String modelNumberLocation,
                                                             String priceLocation,
                                                             String vendor,
                                                             String currency) {
        try {
            Thread.sleep(sleepingConstant);
            return genericVendorScraper
                    .scrapeProductData(url, modelNumberLocation, priceLocation)
                    .map(scrapedData ->
                            genericScrapingService.createGenericPricePoint(scrapedData, vendor, currency));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }
}
