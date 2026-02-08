package com.price_tracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GPUDTO {

    private String modelNumber;
    private String name;
    private String chipManufacturer;
    private String boardManufacturer;
    private int videoMemory;
    private BigDecimal price;
}
