package com.price_tracker.domain.dto.hybrid_dtos;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GPUWorkstationDataAndPricePointDTO {

    private GPUWorkstationDTO gpuWorkstationDTO;
    private List<GenericPricePointDTO> gpuWorkstationPricePointDTOList;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
