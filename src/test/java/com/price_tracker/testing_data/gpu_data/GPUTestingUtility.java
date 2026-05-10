package com.price_tracker.testing_data.gpu_data;

import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.mappers.product_mappers.GPUMapper;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_ASUS_5070TI;
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.*;

@Component
@AllArgsConstructor
public class GPUTestingUtility {

    private final GPUMapper gpuMapper;

    /// SAMPLE ENTITIES/DTOS
    public GPUDTO createTestGPU() {
        return gpuMapper.mapTo(GPUEntity.builder()
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .chip(TESTING_GPU_CHIP)
                .chipManufacturer(TESTING_GPU_CHIP_MANUFACTURER)
                .boardManufacturer(TESTING_GPU_BOARD_MANUFACTURER)
                .name(TESTING_GPU_NAME)
                .isActive(true)
                .build());
    }

    public GPUDTO createSecondTestGPU() {
        return gpuMapper.mapTo(GPUEntity.builder()
                .modelNumber(SECOND_TESTING_GPU_MODEL_NUMBER)
                .chip(TESTING_GPU_CHIP)
                .chipManufacturer(TESTING_GPU_CHIP_MANUFACTURER)
                .boardManufacturer(TESTING_GPU_BOARD_MANUFACTURER)
                .name(SECOND_TESTING_GPU_NAME)
                .isActive(true)
                .build());
    }

    public List<GPUDTO> createListOfGPUs() {
        ArrayList<GPUDTO> gpuDTOs = new ArrayList<>();
        gpuDTOs.add(createTestGPU());
        gpuDTOs.add(createSecondTestGPU());
        return gpuDTOs;
    }

    /// SAMPLE PRODUCTS
    public UmartProductEntity createTestUmartGPU() {
        return UmartProductEntity.builder()
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .vendor(UMART)
                .url(UMART_ASUS_5070TI)
                .build();
    }

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleGPUPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_GPU_PRICE))
                .build();
    }
}