package com.price_tracker.webscraper;

import com.price_tracker.domain.entities.GPUPricePoint;

public interface GPUScraper {

    String[] scrapeGPUData(String url);

    boolean validateScrapedData(String[] scrapedData);

    GPUPricePoint createGPUPricePoint(String[] scrapedData);
}
