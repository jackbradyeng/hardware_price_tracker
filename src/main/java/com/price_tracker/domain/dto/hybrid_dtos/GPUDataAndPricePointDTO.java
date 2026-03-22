package com.price_tracker.domain.dto.hybrid_dtos;

import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.mappers.price_point_mappers.GPUPricePointMapper;
import com.price_tracker.mappers.product_mappers.GPUMapper;
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
