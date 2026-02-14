package com.price_tracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GPUDTO {

    private String modelNumber;
    private String chip;
    private String chipManufacturer;
    private String boardManufacturer;
    private String name;
    private boolean isActive = true;
}
