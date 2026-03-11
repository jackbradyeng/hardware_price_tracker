package com.price_tracker.webscraper.dtos;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ScrapedDataDTO(String modelNumber, BigDecimal price) {

}
