package com.price_tracker.testing_data.vendor_data;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
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