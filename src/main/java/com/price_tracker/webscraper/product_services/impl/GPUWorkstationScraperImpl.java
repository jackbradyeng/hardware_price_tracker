package com.price_tracker.webscraper.product_services.impl;

import com.price_tracker.domain.entities.GPUWorkstationPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.GPUWorkstationScraper;
import com.price_tracker.webscraper.vendor_templates.GenericUmartScraper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import static com.price_tracker.constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.VendorConstants.UMART;

@EqualsAndHashCode(callSuper = true)
@Data
@Service
@Log
public class GPUWorkstationScraperImpl extends GenericUmartScraper implements GPUWorkstationScraper {

    @Override
    public GPUWorkstationPricePoint createGPUWorkstationPricePoint(ScrapedDataDTO scrapedDataDTO) {

        return GPUWorkstationPricePoint.builder()
                .modelNumber(scrapedDataDTO.modelNumber())
                .vendor(UMART)
                .currency(AUD)
                .price(scrapedDataDTO.price())
                .scrapedAt(LocalDateTime.now())
                .build();
    }
}
