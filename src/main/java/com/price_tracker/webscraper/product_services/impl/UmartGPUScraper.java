package com.price_tracker.webscraper.product_services.impl;

import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.GPUScraper;
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
public class UmartGPUScraper extends GenericUmartScraper implements GPUScraper {

    @Override
    public GPUPricePoint createGPUPricePoint(ScrapedDataDTO scrapedData) {

        return GPUPricePoint.builder()
                .modelNumber(scrapedData.modelNumber())
                .vendor(UMART)
                .currency(AUD)
                .price(scrapedData.price())
                .scrapedAt(LocalDateTime.now())
                .build();
    }
}
