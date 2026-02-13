package com.price_tracker.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import static com.price_tracker.constants.DBTableNames.RAM_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = RAM_TABLE_NAME)
public class RAM {

    @Id
    private String id;
    private String name;
    private String brand;
    private BigDecimal price;
}
