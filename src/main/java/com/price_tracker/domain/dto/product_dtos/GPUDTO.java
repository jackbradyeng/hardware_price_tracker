package com.price_tracker.domain.dto.product_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GPUDTO {

    @NotBlank
    private String modelNumber;
    @NotBlank @Size(max = 300)
    private String chip;
    @NotBlank @Size(max = 75)
    private String chipManufacturer;
    @NotBlank @Size(max = 75)
    private String boardManufacturer;
    @NotBlank @Size(max = 350)
    private String name;
    @NotNull
    private Boolean isActive;
}