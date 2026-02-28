package com.price_tracker;

import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.domain.entities.RAMEntity;
import com.price_tracker.domain.entities.UmartProductEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import static com.price_tracker.constants.TestingConstants.*;
import static com.price_tracker.constants.WebDomainNames.UMART_ASUS_5070TI;
import static com.price_tracker.constants.WebDomainNames.UMART_KINGSTON_KINGSTON_F64G;

@Data
@Component
@NoArgsConstructor
public class TestDataUtility {

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
