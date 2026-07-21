package com.priceTracker.domain.entities.productEntities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.WORKSTATION_GPU_TABLE_NAME;

/** Memory measured in gigabytes; memory-interface in bits; memory bandwidth in gigabytes per second; and max power in
 * watts. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = WORKSTATION_GPU_TABLE_NAME)
public class GPUWorkstationEntity {

    @Id
    private String modelNumber;
    private String name;
    private String chipManufacturer;
    private Integer gpuMemory;
    private Integer memoryInterface;
    private Integer memoryBandwidth;
    private Integer cudaCores;
    private Integer tensorCores;
    private Integer raytracingCores;
    private Integer maxPower;
    private String systemInterface;
    private Boolean isActive = true;
}