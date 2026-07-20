package com.priceTracker.domain.entities.productEntities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.priceTracker.constants.otherConstants.DatabaseConstants.NVME_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = NVME_TABLE_NAME)
public class NVMEEntity {

    @Id
    private String modelNumber;
    private String name;
    private String brand;
    private Integer capacity;
    private Integer sequentialRead;
    private Integer sequentialWrite;
    private Long meanTimeBetweenFailures;
    private String storageInterface;
    private Boolean includesHeatSink;
    private Boolean isActive;
}
