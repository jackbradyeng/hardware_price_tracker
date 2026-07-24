package com.priceTracker.domain.dto.pricePointDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A translation layer between a generic price point and it's respective entity. <strong> NOTE: </strong>
 * GenericPricePointDTOs should be mapped to [Product]PricePoint entities using the MapperFactory abstraction.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericPricePointDTO {

    @NotNull
    private Long id;
    @NotBlank
    private String modelNumber;
    @NotBlank
    private String vendor;
    @NotBlank
    private String currency;
    @NotNull
    private BigDecimal price;
    @NotNull
    private LocalDateTime scrapedAt;
}