package com.price_tracker.domain.dto.vendor_dtos;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String vendor;
    @NotBlank
    private String productType;
    @NotBlank
    private String modelNumber;
    @NotBlank
    private String url;
}