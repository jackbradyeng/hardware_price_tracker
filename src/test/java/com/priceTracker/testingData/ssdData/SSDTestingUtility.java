package com.priceTracker.testingData.ssdData;

import com.priceTracker.domain.dto.productDTOs.SSDDTO;
import com.priceTracker.domain.entities.productEntities.SSDEntity;
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
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_CRUCIAL_BX500_1TB;
import static com.priceTracker.testingData.ssdData.SSDTestingData.*;

@Component
public class SSDTestingUtility {

    private final GenericMapper<SSDEntity, SSDDTO> ssdMapper;

    @Autowired
    public SSDTestingUtility(MapperFactory mapperFactory) {
        this.ssdMapper = mapperFactory.create(SSDEntity.class, SSDDTO.class);
    }

    /// SAMPLE ENTITIES/DTOS
    public SSDDTO createTestSSD() {
        return ssdMapper.mapTo(SSDEntity.builder()
                .modelNumber(TESTING_SSD_MODEL_NUMBER)
                .name(TESTING_SSD_NAME)
                .brand(TESTING_SSD_BRAND)
                .capacity(TESTING_SSD_CAPACITY)
                .sequentialRead(TESTING_SSD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_SSD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_SSD_MTBF)
                .storageInterface(TESTING_SSD_STORAGE_INTERFACE)
                .isActive(true)
                .build());
    }

    public SSDDTO createSecondTestSSD() {
        return ssdMapper.mapTo(SSDEntity.builder()
                .modelNumber(SECOND_TESTING_SSD_MODEL_NUMBER)
                .name(SECOND_TESTING_SSD_NAME)
                .brand(TESTING_SSD_BRAND)
                .capacity(TESTING_SSD_CAPACITY)
                .sequentialRead(TESTING_SSD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_SSD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_SSD_MTBF)
                .storageInterface(TESTING_SSD_STORAGE_INTERFACE)
                .isActive(true)
                .build());
    }

    public List<SSDDTO> createListOfSSDs() {
        ArrayList<SSDDTO> ssdDTOs = new ArrayList<>();
        ssdDTOs.add(createTestSSD());
        ssdDTOs.add(createSecondTestSSD());
        return ssdDTOs;
    }

    /// SAMPLE PRODUCTS
    public UmartProductEntity createTestUmartSSD() {
        return UmartProductEntity.builder()
                .productType(PRODUCT_TYPE_SSD)
                .modelNumber(TESTING_SSD_MODEL_NUMBER)
                .vendor(UMART)
                .url(UMART_CRUCIAL_BX500_1TB)
                .build();
    }

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleSSDPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_SSD_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_SSD_PRICE))
                .build();
    }
}