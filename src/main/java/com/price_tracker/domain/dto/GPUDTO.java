package com.price_tracker.domain.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GPUDTO {

    @Id
    private String modelNumber;
    private String name;
    private String brand;
    private BigDecimal price;
}
