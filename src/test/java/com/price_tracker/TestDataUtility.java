package com.price_tracker;

import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.dto.vendor_dtos.UmartProductDTO;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.mappers.product_mappers.RAMMapper;
import com.price_tracker.mappers.vendor_mappers.UmartProductMapper;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.price_tracker.testing_data.TestingConstants.*;
import static com.price_tracker.testing_data.UmartWebDomainNames.UMART_KINGSTON_KINGSTON_F64G;

@Data
@Component
@AllArgsConstructor
public class TestDataUtility {

    private final RAMMapper ramMapper;
    private final GPUTestingUtility gpuTestingUtility;
    private final UmartProductMapper umartProductMapper;

    public List<RAMDTO> createListOfRAM() {
        ArrayList<RAMDTO> ramDTOs = new ArrayList<>();
        ramDTOs.add(ramMapper.mapTo(createTestRAM()));
        ramDTOs.add(ramMapper.mapTo(createSecondTestRAM()));
        return ramDTOs;
    }

    /// SAMPLE PRODUCTS
    public UmartProductEntity createTestUmartRAM() {
        return UmartProductEntity.builder()
                .productType(PRODUCT_TYPE_RAM)
                .modelNumber(TESTING_RAM_MODEL_NUMBER)
                .vendor(TESTING_VENDOR_UMART)
                .url(UMART_KINGSTON_KINGSTON_F64G)
                .build();
    }

    public List<UmartProductDTO> createTestUmartProducts() {
        ArrayList<UmartProductDTO> umartProductDTOs = new ArrayList<>();
        umartProductDTOs.add(
                umartProductMapper.mapTo(gpuTestingUtility.createTestUmartGPU())
        );
        umartProductDTOs.add(umartProductMapper.mapTo(createTestUmartRAM()));
        return umartProductDTOs;
    }

    public RAMEntity createTestRAM() {
        return RAMEntity.builder()
                .modelNumber(TESTING_RAM_MODEL_NUMBER)
                .name(TESTING_RAM_NAME)
                .brand(TESTING_RAM_BRAND)
                .standard(TESTING_RAM_STANDARD)
                .latency(TESTING_RAM_LATENCY)
                .volume(TESTING_RAM_VOLUME)
                .dimmCount(TESTING_RAM_DIMM_COUNT)
                .clockRate(TESTING_RAM_CLOCK_RATE)
                .voltage(TESTING_RAM_VOLTAGE)
                .isActive(true)
                .build();
    }

    public RAMEntity createSecondTestRAM() {
        return RAMEntity.builder()
                .modelNumber(SECOND_TESTING_RAM_MODEL_NUMBER)
                .name(SECOND_TESTING_RAM_NAME)
                .brand(SECOND_TESTING_RAM_BRAND)
                .standard(TESTING_RAM_STANDARD)
                .latency(TESTING_RAM_LATENCY)
                .volume(SECOND_TESTING_RAM_VOLUME)
                .dimmCount(TESTING_RAM_DIMM_COUNT)
                .clockRate(TESTING_RAM_CLOCK_RATE)
                .voltage(SECOND_TESTING_RAM_VOLTAGE)
                .isActive(true)
                .build();
    }

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
        return GPUWorkstationDTO.builder()
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

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleRAMPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_RAM_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_RAM_PRICE))
                .build();
    }

    public ScrapedDataDTO createSampleWSGPUPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_WS_GPU_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_WS_GPU_PRICE))
                .build();
    }
}
