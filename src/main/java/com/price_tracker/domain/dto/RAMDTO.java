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
public class RAMDTO {

    @Id
    private String id;
    private String name;
    private String brand;
    private BigDecimal price;
}
