package com.price_tracker.testing_data.ssd_data;

import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
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
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_CRUCIAL_BX500_1TB;
import static com.price_tracker.testing_data.ssd_data.SSDTestingData.*;

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