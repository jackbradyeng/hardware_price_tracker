package com.price_tracker.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static com.price_tracker.constants.DatabaseTableNames.CPU_PRICE_HISTORY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = CPU_PRICE_HISTORY)
public class CPUPricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelNumber;
    private String vendor;
    private String currency;
    private BigDecimal price;
    private LocalDateTime scrapedAt;
}
