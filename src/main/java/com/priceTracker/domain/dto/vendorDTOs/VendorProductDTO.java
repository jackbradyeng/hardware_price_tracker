package com.priceTracker.domain.dto.vendorDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorProductDTO {

    /* IDs are automatically generated for all vendor products. */
    private Long id;
    @NotBlank @Size(min = 1, max = 75)
    private String vendor;
    @NotBlank @Size(min = 1, max = 50)
    private String productType;
    @NotBlank
    private String modelNumber;
    @NotBlank
    private String url;
}