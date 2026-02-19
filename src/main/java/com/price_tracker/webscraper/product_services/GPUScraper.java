package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.entities.GPUPricePoint;

public interface GPUScraper {

    GPUPricePoint createGPUPricePoint(String[] scrapedData);
}
