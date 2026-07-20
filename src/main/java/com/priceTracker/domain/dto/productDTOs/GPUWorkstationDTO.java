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
public class GPUWorkstationDTO {

    @NotBlank
    private String modelNumber;
    @NotBlank @Size(max = 300)
    private String name;
    @NotBlank @Size(max = 75)
    private String chipManufacturer;
    @NotNull @Positive
    private Integer gpuMemory;
    @NotNull @Positive
    private Integer memoryInterface; // in bits
    @NotNull @Positive
    private Integer memoryBandwidth; // gigabytes/second
    @NotNull @Positive
    private Integer cudaCores;
    @NotNull @Positive
    private Integer tensorCores;
    @NotNull @Positive
    private Integer raytracingCores;
    @NotNull @Positive @Max(10000)
    private Integer maxPower; // no GPU should consume 10KW of power
    @NotBlank @Size(max = 50)
    private String systemInterface;
    @NotNull
    private Boolean isActive;
}