package com.priceTracker.testingData.vendorData;

import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.testingData.gpuData.GPUTestingUtility;
import com.priceTracker.testingData.ramData.RAMTestingUtility;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class UmartTestDataUtility {

    private final GPUTestingUtility gpuTestingUtility;
    private final RAMTestingUtility ramTestingUtility;

    public List<VendorProductDTO> createTestUmartProducts() {
        ArrayList<VendorProductDTO> vendorProductDTOS = new ArrayList<>();
        vendorProductDTOS.add(gpuTestingUtility.createTestUmartGPU());
        vendorProductDTOS.add(ramTestingUtility.createTestUmartRAM());
        return vendorProductDTOS;
    }
}