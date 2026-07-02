package com.price_tracker.testing_data.vendor_data;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@AllArgsConstructor
public class UmartTestDataUtility {

    private final GPUTestingUtility gpuTestingUtility;
    private final RAMTestingUtility ramTestingUtility;
    private final GenericMapper<UmartProductEntity, VendorProductDTO> umartProductMapper;

    @Autowired
    public UmartTestDataUtility(GPUTestingUtility gpuTestingUtility, RAMTestingUtility ramTestingUtility,
                                MapperFactory mapperFactory) {
        this.gpuTestingUtility = gpuTestingUtility;
        this.ramTestingUtility = ramTestingUtility;
        this.umartProductMapper = mapperFactory.create(UmartProductEntity.class, VendorProductDTO.class);
    }

    public List<VendorProductDTO> createTestUmartProducts() {
        ArrayList<VendorProductDTO> vendorProductDTOS = new ArrayList<>();
        vendorProductDTOS.add(
                umartProductMapper.mapTo(gpuTestingUtility.createTestUmartGPU())
        );
        vendorProductDTOS.add(
                umartProductMapper.mapTo(ramTestingUtility.createTestUmartRAM())
        );
        return vendorProductDTOS;
    }
}
