package com.price_tracker.domain.entities.price_point_entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static com.price_tracker.constants.DatabaseConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GPU_PRICE_HISTORY)
public class GPUPricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GPU_PRICE_SEQUENCE)
    @SequenceGenerator(
            name = GPU_PRICE_SEQUENCE,
            sequenceName = GPU_PRICE_SEQUENCE,
            allocationSize = 1
    )
    private Long id;
    private String modelNumber;
    private String vendor;
    private String currency;
    private BigDecimal price;
    private LocalDateTime scrapedAt;
}
