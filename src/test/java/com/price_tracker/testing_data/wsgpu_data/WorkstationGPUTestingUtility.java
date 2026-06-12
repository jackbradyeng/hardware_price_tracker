package com.price_tracker.testing_data.wsgpu_data;

import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;

import java.math.BigDecimal;

import static com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingData.*;

@Component
public class WorkstationGPUTestingUtility {

    private final GenericMapper<GPUWorkstationEntity, GPUWorkstationDTO> gpuWorkstationMapper;

    @Autowired
    public WorkstationGPUTestingUtility(MapperFactory mapperFactory) {
        this.gpuWorkstationMapper = mapperFactory.create(GPUWorkstationEntity.class, GPUWorkstationDTO.class);
    }

    /// SAMPLE ENTITIES/DTOS
    public GPUWorkstationEntity createTestWorkstationGPU() {
        return GPUWorkstationEntity.builder()
                .modelNumber(TESTING_WS_GPU_MODEL_NUMBER)
                .name(TESTING_WS_GPU_NAME)
                .chipManufacturer(TESTING_WS_GPU_CHIP_MANUFACTURER)
                .gpuMemory(TESTING_WS_GPU_MEMORY)
                .memoryInterface(TESTING_WS_GPU_MEMORY_INTERFACE)
                .memoryBandwidth(TESTING_WS_GPU_MEMORY_BANDWIDTH)
                .cudaCores(TESTING_WS_GPU_CUDA_CORES)
                .tensorCores(TESTING_WS_GPU_TENSOR_CORES)
                .raytracingCores(TESTING_WS_GPU_RT_CORES)
                .systemInterface(TESTING_WS_GPU_SYS_INTERFACE)
                .isActive(true)
                .build();
    }

    public GPUWorkstationDTO createTestWorkstationGPUDTO() {
        return gpuWorkstationMapper.mapTo(createTestWorkstationGPU());
    }

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleWSGPUPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_WS_GPU_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_WS_GPU_PRICE))
                .build();
    }
}
