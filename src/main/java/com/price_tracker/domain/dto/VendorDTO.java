package com.price_tracker.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorDTO {

    private String vendor;
    private String primaryLocation;
    private String primaryCurrency;
    @JsonProperty("homeURL")
    private String homeURL;
    private Boolean activeStatus;
}
