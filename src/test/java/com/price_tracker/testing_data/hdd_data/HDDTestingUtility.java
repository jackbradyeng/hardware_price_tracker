package com.price_tracker.testing_data.hdd_data;

import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.domain.entities.product_entities.HDDEntity;
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
import static com.price_tracker.testing_data.vendor_data.VendorWebDomainNames.UMART_SEAGATE_ST2000DM005;
import static com.price_tracker.testing_data.hdd_data.HDDTestingData.*;

@Component
public class HDDTestingUtility {

    private final GenericMapper<HDDEntity, HDDDTO> hddMapper;

    @Autowired
    public HDDTestingUtility(MapperFactory mapperFactory) {
        this.hddMapper = mapperFactory.create(HDDEntity.class, HDDDTO.class);
    }

    /// SAMPLE ENTITIES/DTOS
    public HDDDTO createTestHDD() {
        return hddMapper.mapTo(HDDEntity.builder()
                .modelNumber(TESTING_HDD_MODEL_NUMBER)
                .name(TESTING_HDD_NAME)
                .brand(TESTING_HDD_BRAND)
                .capacity(TESTING_HDD_CAPACITY)
                .sequentialRead(TESTING_HDD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_HDD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_HDD_MTBF)
                .rpm(TESTING_HDD_RPM)
                .cache(TESTING_HDD_CACHE)
                .storageInterface(TESTING_HDD_STORAGE_INTERFACE)
                .formFactor(TESTING_HDD_FORM_FACTOR)
                .isActive(true)
                .build());
    }

    public HDDDTO createSecondTestHDD() {
        return hddMapper.mapTo(HDDEntity.builder()
                .modelNumber(SECOND_TESTING_HDD_MODEL_NUMBER)
                .name(SECOND_TESTING_HDD_NAME)
                .brand(TESTING_HDD_BRAND)
                .capacity(TESTING_HDD_CAPACITY)
                .sequentialRead(TESTING_HDD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_HDD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_HDD_MTBF)
                .rpm(TESTING_HDD_RPM)
                .cache(TESTING_HDD_CACHE)
                .storageInterface(TESTING_HDD_STORAGE_INTERFACE)
                .formFactor(TESTING_HDD_FORM_FACTOR)
                .isActive(true)
                .build());
    }

    public List<HDDDTO> createListOfHDDs() {
        ArrayList<HDDDTO> hddDTOs = new ArrayList<>();
        hddDTOs.add(createTestHDD());
        hddDTOs.add(createSecondTestHDD());
        return hddDTOs;
    }

    /// SAMPLE PRODUCTS
    public UmartProductEntity createTestUmartHDD() {
        return UmartProductEntity.builder()
                .productType(PRODUCT_TYPE_HDD)
                .modelNumber(TESTING_HDD_MODEL_NUMBER)
                .vendor(UMART)
                .url(UMART_SEAGATE_ST2000DM005)
                .build();
    }

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleHDDPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_HDD_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_HDD_PRICE))
                .build();
    }
}