package com.priceTracker.domain.dto.hybridDTOs;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.dto.productDTOs.HDDDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HDDDataAndPricePointDTO {

    private HDDDTO hddDTO;
    private List<GenericPricePointDTO> hddPricePointDTOList;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}