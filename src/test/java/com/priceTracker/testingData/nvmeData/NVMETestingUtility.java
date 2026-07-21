package com.priceTracker.testingData.nvmeData;

import com.priceTracker.domain.dto.productDTOs.NVMEDTO;
import com.priceTracker.domain.entities.productEntities.NVMEEntity;
import com.priceTracker.domain.entities.vendorEntities.UmartProductEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.priceTracker.constants.vendorConstants.VendorNames.UMART;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_CRUCIAL_P510_1TB;
import static com.priceTracker.testingData.nvmeData.NVMETestingData.*;

@Component
public class NVMETestingUtility {

    private final GenericMapper<NVMEEntity, NVMEDTO> nvmeMapper;

    @Autowired
    public NVMETestingUtility(MapperFactory mapperFactory) {
        this.nvmeMapper = mapperFactory.create(NVMEEntity.class, NVMEDTO.class);
    }

    /// SAMPLE ENTITIES/DTOS
    public NVMEDTO createTestNVME() {
        return nvmeMapper.mapTo(NVMEEntity.builder()
                .modelNumber(TESTING_NVME_MODEL_NUMBER)
                .name(TESTING_NVME_NAME)
                .brand(TESTING_NVME_BRAND)
                .capacity(TESTING_NVME_CAPACITY)
                .sequentialRead(TESTING_NVME_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_NVME_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_NVME_MTBF)
                .storageInterface(TESTING_NVME_STORAGE_INTERFACE)
                .includesHeatSink(TESTING_NVME_INCLUDES_HEAT_SINK)
                .isActive(true)
                .build());
    }

    public NVMEDTO createSecondTestNVME() {
        return nvmeMapper.mapTo(NVMEEntity.builder()
                .modelNumber(SECOND_TESTING_NVME_MODEL_NUMBER)
                .name(SECOND_TESTING_NVME_NAME)
                .brand(TESTING_NVME_BRAND)
                .capacity(TESTING_NVME_CAPACITY)
                .sequentialRead(TESTING_NVME_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_NVME_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_NVME_MTBF)
                .storageInterface(TESTING_NVME_STORAGE_INTERFACE)
                .includesHeatSink(TESTING_NVME_INCLUDES_HEAT_SINK)
                .isActive(true)
                .build());
    }

    public List<NVMEDTO> createListOfNVMEs() {
        ArrayList<NVMEDTO> nvmeDTOs = new ArrayList<>();
        nvmeDTOs.add(createTestNVME());
        nvmeDTOs.add(createSecondTestNVME());
        return nvmeDTOs;
    }

    /// SAMPLE PRODUCTS
    public UmartProductEntity createTestUmartNVME() {
        return UmartProductEntity.builder()
                .productType(PRODUCT_TYPE_NVME)
                .modelNumber(TESTING_NVME_MODEL_NUMBER)
                .vendor(UMART)
                .url(UMART_CRUCIAL_P510_1TB)
                .build();
    }

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleNVMEPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_NVME_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_NVME_PRICE))
                .build();
    }
}