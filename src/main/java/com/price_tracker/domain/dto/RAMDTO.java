package com.price_tracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RAMDTO {

    private String modelNumber;
    private String name;
    private String brand;
    private Integer volume;
    private Integer dimmCount;
    private Integer clockRate;
    private String latency;
    private Double voltage;
    private Boolean isActive = true;
}
