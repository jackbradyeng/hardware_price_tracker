package com.price_tracker;

import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.domain.dto.RAMDTO;
import com.price_tracker.domain.dto.UmartProductDTO;
import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.domain.entities.RAMEntity;
import com.price_tracker.domain.entities.UmartProductEntity;
import com.price_tracker.mappers.impl.GPUMapper;
import com.price_tracker.mappers.impl.RAMMapper;
import com.price_tracker.mappers.impl.UmartProductMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.price_tracker.constants.TestingConstants.*;
import static com.price_tracker.constants.WebDomainNames.UMART_ASUS_5070TI;
import static com.price_tracker.constants.WebDomainNames.UMART_KINGSTON_KINGSTON_F64G;

@Data
@Component
@AllArgsConstructor
public class TestDataUtility {

    private final GPUMapper gpuMapper;
    private final RAMMapper ramMapper;
    private final UmartProductMapper umartProductMapper;

    public GPUEntity createTestGPU() {
        return GPUEntity.builder()
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .chip(TESTING_GPU_CHIP)
                .chipManufacturer(TESTING_GPU_CHIP_MANUFACTURER)
                .boardManufacturer(TESTING_GPU_BOARD_MANUFACTURER)
                .name(TESTING_GPU_NAME)
                .isActive(true)
                .build();
    }

    public GPUEntity createSecondTestGPU() {
        return GPUEntity.builder()
                .modelNumber(SECOND_TESTING_GPU_MODEL_NUMBER)
                .chip(TESTING_GPU_CHIP)
                .chipManufacturer(TESTING_GPU_CHIP_MANUFACTURER)
                .boardManufacturer(TESTING_GPU_BOARD_MANUFACTURER)
                .name(SECOND_TESTING_GPU_NAME)
                .isActive(true)
                .build();
    }

    public List<GPUDTO> createListOfGPUs() {
        ArrayList<GPUDTO> gpuDTOs = new ArrayList<>();
        gpuDTOs.add(gpuMapper.mapTo(createTestGPU()));
        gpuDTOs.add(gpuMapper.mapTo(createSecondTestGPU()));
        return gpuDTOs;
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
                .clockRate(TESTING_RAM_CLOCKRATE)
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
                .clockRate(TESTING_RAM_CLOCKRATE)
                .voltage(SECOND_TESTING_RAM_VOLTAGE)
                .isActive(true)
                .build();
    }

    public List<RAMDTO> createListOfRAM() {
        ArrayList<RAMDTO> ramDTOs = new ArrayList<>();
        ramDTOs.add(ramMapper.mapTo(createTestRAM()));
        ramDTOs.add(ramMapper.mapTo(createSecondTestRAM()));
        return ramDTOs;
    }

    public UmartProductEntity createTestUmartGPU() {
        return UmartProductEntity.builder()
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .vendor(TESTING_VENDOR_UMART)
                .url(UMART_ASUS_5070TI)
                .build();
    }

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
        umartProductDTOs.add(umartProductMapper.mapTo(createTestUmartGPU()));
        umartProductDTOs.add(umartProductMapper.mapTo(createTestUmartRAM()));
        return umartProductDTOs;
    }

    public String[] createSampleTestScrapedGPUData() {
        String[] scrapedData = new String[2];
        scrapedData[0] = TESTING_GPU_MODEL_NUMBER;
        scrapedData[1] = TESTING_GPU_PRICE;
        return scrapedData;
    }

    public String[] createSampleTestScrapedRAMData() {
        String[] scrapedData = new String[2];
        scrapedData[0] = TESTING_RAM_MODEL_NUMBER;
        scrapedData[1] = TESTING_RAM_PRICE;
        return scrapedData;
    }
}
