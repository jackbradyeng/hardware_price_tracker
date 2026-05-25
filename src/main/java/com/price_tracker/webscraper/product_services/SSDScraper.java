package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;

public interface SSDScraper {

    SSDPricePoint createSSDPricePoint(ScrapedDataDTO scrapedData);
}