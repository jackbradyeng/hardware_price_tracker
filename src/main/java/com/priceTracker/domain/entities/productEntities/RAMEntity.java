package com.priceTracker.domain.entities.productEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.RAM_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = RAM_TABLE_NAME)
public class RAMEntity {

    @Id
    private String modelNumber;
    private String name;
    private String brand;
    private String standard;
    private String latency;
    @Column(nullable = false)
    private Integer volume;
    @Column(nullable = false)
    private Integer dimmCount;
    @Column(nullable = false)
    private Integer clockRate; // measured in MHZ
    private Double voltage; // measured in volts
    @Column(nullable = false)
    private Boolean isActive = true; // active status flag, tells the scraper whether to look for the given model
}