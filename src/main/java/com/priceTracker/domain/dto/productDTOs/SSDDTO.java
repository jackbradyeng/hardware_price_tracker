package com.priceTracker.domain.dto.productDTOs;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SSDDTO {

    @NotBlank
    private String modelNumber;
    @NotBlank @Size(max = 300)
    private String name;
    @NotBlank @Size(max = 70)
    private String brand;
    @NotNull @Positive
    private Integer capacity;
    @Positive
    private Integer sequentialRead;
    @Positive
    private Integer sequentialWrite;
    @Positive
    private Long meanTimeBetweenFailures;
    @NotBlank @Size(max = 50)
    private String storageInterface;
    @NotNull
    private Boolean isActive;
}