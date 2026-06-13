package com.price_tracker.testing_data.vendor_data;

import com.price_tracker.domain.dto.vendor_dtos.UmartProductDTO;
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
    private final GenericMapper<UmartProductEntity, UmartProductDTO> umartProductMapper;

    @Autowired
    public UmartTestDataUtility(GPUTestingUtility gpuTestingUtility, RAMTestingUtility ramTestingUtility,
                                MapperFactory mapperFactory) {
        this.gpuTestingUtility = gpuTestingUtility;
        this.ramTestingUtility = ramTestingUtility;
        this.umartProductMapper = mapperFactory.create(UmartProductEntity.class, UmartProductDTO.class);
    }

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
