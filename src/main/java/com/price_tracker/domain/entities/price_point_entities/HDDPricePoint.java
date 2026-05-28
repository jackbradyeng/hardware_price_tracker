package com.price_tracker.domain.entities.price_point_entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static com.price_tracker.constants.other_constants.DatabaseConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = HDD_PRICE_HISTORY)
public class HDDPricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HDD_PRICE_SEQUENCE)
    @SequenceGenerator(
            name = HDD_PRICE_SEQUENCE,
            sequenceName = HDD_PRICE_SEQUENCE,
            allocationSize = 1
    )
    private Long id;
    private String modelNumber;
    private String vendor;
    private String currency;
    private BigDecimal price;
    private LocalDateTime scrapedAt;
}