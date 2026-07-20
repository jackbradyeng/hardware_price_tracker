package com.priceTracker.testingData.gpuData;

import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.domain.entities.productEntities.GPUEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.priceTracker.constants.vendorConstants.VendorNames.SCORPTEC;
import static com.priceTracker.constants.vendorConstants.VendorNames.UMART;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.SCORPTEC_ASUS_5070TI;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_ASUS_5070TI;
import static com.priceTracker.testingData.gpuData.GPUTestingData.*;

@Component
public class GPUTestingUtility {

    private final GenericMapper<GPUEntity, GPUDTO> gpuMapper;

    @Autowired
    public GPUTestingUtility(MapperFactory mapperFactory) {
        this.gpuMapper = mapperFactory.create(GPUEntity.class, GPUDTO.class);
    }

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
    public VendorProductDTO createTestUmartGPU() {
        return VendorProductDTO.builder()
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .vendor(UMART)
                .url(UMART_ASUS_5070TI)
                .build();
    }

    public VendorProductDTO createTestScorptecGPU() {
        return VendorProductDTO.builder()
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .vendor(SCORPTEC)
                .url(SCORPTEC_ASUS_5070TI)
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