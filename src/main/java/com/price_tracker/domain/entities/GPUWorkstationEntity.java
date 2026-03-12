package com.price_tracker.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import static com.price_tracker.constants.DatabaseTableNames.WORKSTATION_GPU_TABLE_NAME;

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
    private String chipManufacturer; // nvidia-only for now
    private Integer gpuMemory; // in gigabytes
    private Integer memoryInterface; // in bits
    private Integer memoryBandwidth; // gigabytes/second
    private Integer cudaCores;
    private Integer tensorCores;
    private Integer raytracingCores;
    private Integer maxPower; // in watts
    private String systemInterface; //PCIe type
}
