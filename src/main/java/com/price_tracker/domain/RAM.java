package com.price_tracker.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import static com.price_tracker.DBTableNames.RAM_TABLE_NAME;

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

    // @ManyToOne(cascade = CascadeType.ALL)
    private String brand;

    private int volume;
    private int clockRate;
    private BigDecimal price;
}
