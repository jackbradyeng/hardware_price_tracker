package com.price_tracker.domain.dto.hybrid_dtos;

import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GPUDataAndPricePointDTO {

    private GPUDTO gpuDTO;
    private GPUPricePointDTO gpuPricePointDTO;
}
