package com.priceTracker.testingData.ramData;

import com.priceTracker.domain.dto.productDTOs.RAMDTO;
import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.domain.entities.productEntities.RAMEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.priceTracker.constants.vendorConstants.VendorNames.UMART;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_KINGSTON_KINGSTON_F64G;
import static com.priceTracker.testingData.ramData.RAMTestingData.*;

@Component
public class RAMTestingUtility {

    private final GenericMapper<RAMEntity, RAMDTO> ramMapper;

    @Autowired
    public RAMTestingUtility(MapperFactory mapperFactory) {
        this.ramMapper = mapperFactory.create(RAMEntity.class, RAMDTO.class);
    }

    /// SAMPLE ENTITIES/DTOS
    public RAMDTO createTestRAM() {
        return ramMapper.mapTo(RAMEntity.builder()
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
                .build());
    }

    public RAMDTO createSecondTestRAM() {
        return ramMapper.mapTo(RAMEntity.builder()
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
                .build());
    }

    public List<RAMDTO> createListOfRAM() {
        ArrayList<RAMDTO> ramDTOs = new ArrayList<>();
        ramDTOs.add(createTestRAM());
        ramDTOs.add(createSecondTestRAM());
        return ramDTOs;
    }

    /// SAMPLE PRODUCTS
    public VendorProductDTO createTestUmartRAM() {
        return VendorProductDTO.builder()
                .productType(PRODUCT_TYPE_RAM)
                .modelNumber(TESTING_RAM_MODEL_NUMBER)
                .vendor(UMART)
                .url(UMART_KINGSTON_KINGSTON_F64G)
                .build();
    }

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleRAMPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_RAM_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_RAM_PRICE))
                .build();
    }
}