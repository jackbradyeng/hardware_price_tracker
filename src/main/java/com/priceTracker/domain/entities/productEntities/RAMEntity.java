package com.priceTracker.domain.entities.productEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.RAM_TABLE_NAME;

/** Volume measured in gigabytes; clock rate in hertz; and voltage in volts. **/
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
    private Integer clockRate;
    private Double voltage;
    @Column(nullable = false)
    private Boolean isActive = true;
}