package com.price_tracker.testing_data.vendor_data;

import com.price_tracker.domain.dto.vendor_dtos.UmartProductDTO;
import com.price_tracker.mappers.vendor_mappers.UmartProductMapper;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@AllArgsConstructor
public class UmartTestDataUtility {

    private final GPUTestingUtility gpuTestingUtility;
    private final RAMTestingUtility ramTestingUtility;
    private final UmartProductMapper umartProductMapper;

    public List<UmartProductDTO> createTestUmartProducts() {
        ArrayList<UmartProductDTO> umartProductDTOs = new ArrayList<>();
        umartProductDTOs.add(
                umartProductMapper.mapTo(gpuTestingUtility.createTestUmartGPU())
        );
        umartProductDTOs.add(
                umartProductMapper.mapTo(ramTestingUtility.createTestUmartRAM())
        );
        return umartProductDTOs;
    }
}
