package com.priceTracker.testingData.wsgpuData;

import com.priceTracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.priceTracker.domain.dto.productDTOs.GPUWorkstationDTO;
import com.priceTracker.domain.entities.productEntities.GPUWorkstationEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import java.math.BigDecimal;
import static com.priceTracker.testingData.wsgpuData.WorkstationGPUTestingData.*;

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
                .maxPower(TESTING_WS_GPU_MAX_POWER)
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