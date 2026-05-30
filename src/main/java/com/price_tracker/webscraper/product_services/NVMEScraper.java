package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;

public interface NVMEScraper {

    NVMEPricePoint createNVMEPricePoint(ScrapedDataDTO scrapedData);
}