package com.priceTracker.domain.dto.hybridDTOs;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.dto.productDTOs.CPUDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CPUDataAndPricePointDTO {

    private CPUDTO cpuDTO;
    private List<GenericPricePointDTO> cpuPricePointDTOList;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}