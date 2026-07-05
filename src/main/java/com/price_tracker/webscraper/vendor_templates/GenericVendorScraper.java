package com.price_tracker.webscraper.vendor_templates;

import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import java.util.Optional;

public interface GenericVendorScraper {

    Optional<ScrapedDataDTO> scrapeProductData(String url, String modelNumberLocation, String priceLocation);
}
