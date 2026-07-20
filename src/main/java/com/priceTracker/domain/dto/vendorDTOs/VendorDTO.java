package com.priceTracker.domain.dto.vendorDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class VendorDTO {

    @NotBlank
    private String vendor;
    @NotBlank
    private String primaryLocation;
    @NotBlank @Size(min = 1, max = 10)
    private String primaryCurrency;
    @JsonProperty("homeURL")
    @NotBlank
    private String homeURL;
    @NotNull
    private Boolean activeStatus;
}