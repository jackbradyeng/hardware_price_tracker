package com.priceTracker.domain.dto.productDTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RAMDTO {

    @NotBlank
    private String modelNumber;
    @NotBlank @Size(max = 300)
    private String name;
    @NotBlank @Size(max = 50)
    private String brand;
    @NotBlank
    private String standard;
    @NotNull @Positive
    private Integer volume;
    @NotNull @Positive @Max(12)
    private Integer dimmCount;
    @NotNull @Positive
    private Integer clockRate;
    @NotBlank
    private String latency;
    @NotNull @Positive
    private Double voltage;
    @NotNull
    private Boolean isActive;
}