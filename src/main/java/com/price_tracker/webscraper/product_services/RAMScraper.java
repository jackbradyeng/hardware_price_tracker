package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.entities.RAMPricePoint;

public interface RAMScraper {

    RAMPricePoint createRAMPricePoint(String[] scrapedData);
}
