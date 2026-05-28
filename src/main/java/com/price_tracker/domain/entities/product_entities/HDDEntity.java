package com.price_tracker.domain.entities.product_entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import static com.price_tracker.constants.other_constants.DatabaseConstants.HDD_TABLE_NAME;

@Data
@AllArgsConstructor
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
