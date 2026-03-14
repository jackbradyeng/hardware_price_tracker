package com.price_tracker.webscraper.product_services;

import com.price_tracker.domain.entities.GPUWorkstationPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;

public interface GPUWorkstationScraper {

    GPUWorkstationPricePoint createGPUWorkstationPricePoint(ScrapedDataDTO scrapedDataDTO);
}
