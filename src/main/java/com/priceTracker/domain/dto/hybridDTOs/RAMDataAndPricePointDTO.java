package com.priceTracker.domain.dto.hybridDTOs;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.dto.productDTOs.RAMDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RAMDataAndPricePointDTO {

    private RAMDTO ramDTO;
    private List<GenericPricePointDTO> ramPricePointDTOList;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}