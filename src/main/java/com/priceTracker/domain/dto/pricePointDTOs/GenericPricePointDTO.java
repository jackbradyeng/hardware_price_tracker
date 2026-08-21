package com.priceTracker.domain.dto.pricePointDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    private Long id;
    @NotBlank
    private String productType;
    @NotBlank
    private String modelNumber;
    @NotBlank
    private String vendor;
    @NotBlank @Size(min = 1) @Size(max = 10)
    private String currency;
    @NotNull @Positive
    private BigDecimal price;
    @NotNull
    private LocalDateTime scrapedAt;
}