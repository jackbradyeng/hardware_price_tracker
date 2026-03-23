package com.price_tracker.domain.dto.hybrid_dtos;

import com.price_tracker.domain.dto.price_point_dtos.RAMPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
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
    private List<RAMPricePointDTO> ramPricePointDTOList;
}
