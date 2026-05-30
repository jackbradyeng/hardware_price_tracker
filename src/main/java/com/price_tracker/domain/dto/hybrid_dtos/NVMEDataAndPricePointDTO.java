package com.price_tracker.domain.dto.hybrid_dtos;

import com.price_tracker.domain.dto.price_point_dtos.NVMEPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NVMEDataAndPricePointDTO {

    private NVMEDTO nvmeDTO;
    private List<NVMEPricePointDTO> nvmePricePointDTOList;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}