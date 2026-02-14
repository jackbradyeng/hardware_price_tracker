package com.price_tracker.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.price_tracker.constants.DBTableNames.GPU_TABLE_NAME;

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
    private boolean isActive = true;
}
