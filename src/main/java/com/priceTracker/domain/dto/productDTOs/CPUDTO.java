package com.priceTracker.domain.dto.productDTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CPUDTO {

    @NotBlank
    private String modelNumber;
    @NotBlank @Size(max = 300)
    private String name;
    @NotBlank
    private String chipManufacturer;
    @NotBlank
    private String series;
    @NotNull @Positive
    private Integer cores;
    @NotNull @Positive
    private Integer threads;
    @NotNull @Positive
    private Double baseClock;
    @Positive
    private Double boostClock;
    @Positive
    private Double l1Cache;
    @Positive
    private Double l2Cache;
    @Positive
    private Double l3Cache;
    @NotNull @Positive
    private Integer thermalDesignPower;
    @Positive @Max(150)
    private Integer maxTemperature;
    @Positive
    private Integer maxMemory;
    @NotBlank @Size(max = 50)
    private String memorySupported;
    @NotNull
    private Boolean hasIntegratedGPU;
    @NotNull
    private Boolean isActive;
}