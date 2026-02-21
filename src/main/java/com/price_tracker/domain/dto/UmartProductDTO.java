package com.price_tracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UmartProductDTO {

    private Long id;
    private String vendor;
    private String productType;
    private String modelNumber;
    private String url;
}
