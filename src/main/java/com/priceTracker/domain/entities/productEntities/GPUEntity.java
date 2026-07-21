package com.priceTracker.domain.entities.productEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.GPU_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GPU_TABLE_NAME)
public class GPUEntity {

    @Id
    private String modelNumber;
    private String chip;
    private String chipManufacturer;
    private String boardManufacturer;
    private String name;
    @Column(nullable = false)
    private Boolean isActive = true;
}