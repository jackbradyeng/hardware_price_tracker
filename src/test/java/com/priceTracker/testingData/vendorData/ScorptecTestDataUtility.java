package com.priceTracker.testingData.vendorData;

import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.testingData.cpuData.CPUTestingUtility;
import com.priceTracker.testingData.gpuData.GPUTestingUtility;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class ScorptecTestDataUtility {

    private final CPUTestingUtility cpuTestingUtility;
    private final GPUTestingUtility gpuTestingUtility;

    public List<VendorProductDTO> createTestScorptecProducts() {
        ArrayList<VendorProductDTO> vendorProductDTOS = new ArrayList<>();
        vendorProductDTOS.add(cpuTestingUtility.createTestScorptecCPU());
        vendorProductDTOS.add(gpuTestingUtility.createTestScorptecGPU());
        return vendorProductDTOS;
    }
}