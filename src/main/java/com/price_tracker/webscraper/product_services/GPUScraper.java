package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;

public interface GPUScraper {

    GPUPricePoint createGPUPricePoint(ScrapedDataDTO scrapedData);
}
