package com.price_tracker.domain.dto;

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
public class GPUPricePointDTO {

    private Long id;
    private String modelNumber;
    private BigDecimal price;
    private LocalDateTime scrapedAt;
}
