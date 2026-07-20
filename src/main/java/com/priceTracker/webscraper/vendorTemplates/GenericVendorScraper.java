package com.priceTracker.webscraper.vendorTemplates;

import com.priceTracker.webscraper.dtos.ScrapedDataDTO;
import java.util.Optional;

public interface GenericVendorScraper {

    Optional<ScrapedDataDTO> scrapeProductData(String url, String modelNumberLocation, String priceLocation);
}
