package com.price_tracker.testing_data.nvme_data;

import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;
import static com.price_tracker.testing_data.vendor_data.VendorWebDomainNames.UMART_CRUCIAL_P510_1TB;
import static com.price_tracker.testing_data.nvme_data.NVMETestingData.*;

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