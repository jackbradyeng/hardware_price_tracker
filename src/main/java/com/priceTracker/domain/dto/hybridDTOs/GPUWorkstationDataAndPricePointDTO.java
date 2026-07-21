package com.priceTracker.domain.dto.hybridDTOs;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.dto.productDTOs.GPUWorkstationDTO;
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
