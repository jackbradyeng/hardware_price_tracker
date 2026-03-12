package com.price_tracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GPUWorkstationDTO {

    private String modelNumber;
    private String name;
    private String chipManufacturer;
    private Integer gpuMemory;
    private Integer memoryInterface; // in bits
    private Integer memoryBandwidth; // gigabytes/second
    private Integer cudaCores;
    private Integer tensorCores;
    private Integer raytracingCores;
    private Integer maxPower;
    private String systemInterface;
}
