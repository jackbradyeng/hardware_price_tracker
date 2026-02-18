package com.price_tracker.webscraper;

import com.price_tracker.domain.entities.GPUPricePoint;

public interface GPUScraper {

    GPUPricePoint createGPUPricePoint(String[] scrapedData);
}
