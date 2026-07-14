package com.price_tracker.domain.dto.product_dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HDDDTO {

    @NotBlank
    private String modelNumber;
    @NotBlank @Size(max = 300)
    private String name;
    @NotBlank @Size(max = 75)
    private String brand;
    @NotNull @Positive
    private Integer capacity;
    @Positive
    private Integer sequentialRead;
    @Positive
    private Integer sequentialWrite;
    @Positive
    private Long meanTimeBetweenFailures;
    @NotNull @Positive
    private Integer rpm;
    @Positive
    private Integer cache;
    @NotBlank @Size(max = 50)
    private String storageInterface;
    @NotNull @DecimalMin("2.50") @DecimalMax("5.00")
    private Float formFactor;
    @NotNull
    private Boolean isActive;
}