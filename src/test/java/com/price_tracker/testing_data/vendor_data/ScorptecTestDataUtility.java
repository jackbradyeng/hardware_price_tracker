package com.price_tracker.testing_data.vendor_data;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.domain.entities.vendor_entities.ScorptecProductEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@AllArgsConstructor
public class ScorptecTestDataUtility {

    private final CPUTestingUtility cpuTestingUtility;
    private final GPUTestingUtility gpuTestingUtility;
    private final GenericMapper<ScorptecProductEntity, VendorProductDTO> scorptecProductMapper;

    @Autowired
    public ScorptecTestDataUtility(CPUTestingUtility cpuTestingUtility, GPUTestingUtility gpuTestingUtility,
                                    MapperFactory mapperFactory) {
        this.cpuTestingUtility = cpuTestingUtility;
        this.gpuTestingUtility = gpuTestingUtility;
        this.scorptecProductMapper = mapperFactory.create(ScorptecProductEntity.class, VendorProductDTO.class);
    }

    public List<VendorProductDTO> createTestScorptecProducts() {
        ArrayList<VendorProductDTO> vendorProductDTOS = new ArrayList<>();
        vendorProductDTOS.add(
                scorptecProductMapper.mapTo(cpuTestingUtility.createTestScorptecCPU())
        );
        vendorProductDTOS.add(
                scorptecProductMapper.mapTo(gpuTestingUtility.createTestScorptecGPU())
        );
        return vendorProductDTOS;
    }
}