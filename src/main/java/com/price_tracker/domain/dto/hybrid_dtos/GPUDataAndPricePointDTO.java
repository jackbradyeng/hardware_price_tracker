package com.price_tracker.domain.dto.hybrid_dtos;

import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GPUDataAndPricePointDTO {

    // only returns a SINGLE GPU instance along with it's associated price history
    private GPUDTO gpuDTO;
    private List<GPUPricePointDTO> gpuPricePointDTOList;
}
