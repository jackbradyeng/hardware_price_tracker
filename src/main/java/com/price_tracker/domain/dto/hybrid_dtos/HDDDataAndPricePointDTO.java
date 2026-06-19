package com.price_tracker.domain.dto.hybrid_dtos;

import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.HDDDTO;
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