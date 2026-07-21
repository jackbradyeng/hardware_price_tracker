package com.priceTracker.webscraper.orchestrators;

import java.util.Optional;
import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.webscraper.productServices.GenericScrapingService;
import com.priceTracker.webscraper.vendorTemplates.GenericVendorScraper;

/**
 * A high level abstraction for generating GenericPricePointDTOs irrespective of vendor or product type. Takes in a
 * GenericVendorScraper and GenericScrapingService as parameters, along with the associated fields needed to generate a
 * ScrapedDataDTO and THEN a GenericPricePointDTO.
 */
public interface GenericScrapingOrchestrator {

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
