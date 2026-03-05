package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.entities.CPUPricePoint;

public interface CPUScraper {

    CPUPricePoint createCPUPricePoint(String[] scrapedData);
}
