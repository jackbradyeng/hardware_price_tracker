package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.entities.CPUPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;

public interface CPUScraper {

    CPUPricePoint createCPUPricePoint(ScrapedDataDTO scrapedData);
}
