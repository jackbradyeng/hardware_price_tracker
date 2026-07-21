package com.priceTracker.domain.entities.productEntities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.priceTracker.constants.otherConstants.DatabaseConstants.HDD_TABLE_NAME;

/** Capacity measured in gigabytes; sequential read/write in gigabytes/second; MTBF in hours; and cache measured in
 * megabytes. **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = HDD_TABLE_NAME)
public class HDDEntity {

    @Id
    private String modelNumber;
    private String name;
    private String brand;
    private Integer capacity;
    private Integer sequentialRead;
    private Integer sequentialWrite;
    private Long meanTimeBetweenFailures;
    private Integer rpm;
    private Integer cache;
    private String storageInterface;
    private Float formFactor;
    private Boolean isActive;
}