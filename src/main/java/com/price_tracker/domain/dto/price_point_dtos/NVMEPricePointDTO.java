package com.price_tracker.domain.dto.price_point_dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NVMEPricePointDTO {

    private Long id;
    private String modelNumber;
    private String vendor;
    private String currency;
    private BigDecimal price;
    private LocalDateTime scrapedAt;
}