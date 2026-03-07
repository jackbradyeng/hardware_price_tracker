package com.price_tracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CPUDTO {

    private String modelNumber;
    private String name;
    private String chipManufacturer;
    private String series;
    private Integer cores;
    private Integer threads;
    private Double baseClock;
    private Double boostClock;
    private Double l1Cache;
    private Double l2Cache;
    private Double l3Cache;
    private Integer thermalDesignPower;
    private Integer maxTemperature;
    private Integer maxMemory;
    private String memorySupported;
    private Boolean hasIntegratedGPU;
    private Boolean isActive;
}
