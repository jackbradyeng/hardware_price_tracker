package com.price_tracker.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.price_tracker.constants.DatabaseTableNames.CPU_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = CPU_TABLE_NAME)
public class CPUEntity {

    @Id
    private String modelNumber;
    private String name;
    private String chipManufacturer;
    private String series;
    private Integer cores;
    private Integer threads;
    private Double l1Cache;
    private Double l2Cache;
    private Double l3Cache;
    private Integer maxMemory;
    private String memorySupported;
}
