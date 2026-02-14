package com.price_tracker.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static com.price_tracker.constants.DBTableNames.GPU_PRICE_HISTORY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GPU_PRICE_HISTORY)
public class GPUPricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelNumber;
    private BigDecimal price;
    private LocalDateTime scrapedAt;
}
