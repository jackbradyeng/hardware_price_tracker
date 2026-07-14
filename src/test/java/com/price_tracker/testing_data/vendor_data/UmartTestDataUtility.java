package com.price_tracker.testing_data.vendor_data;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
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