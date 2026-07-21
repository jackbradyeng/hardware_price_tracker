package com.priceTracker.testingData.cpuData;

import com.priceTracker.domain.dto.productDTOs.CPUDTO;
import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.domain.entities.productEntities.CPUEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.webscraper.dtos.ScrapedDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.priceTracker.constants.vendorConstants.VendorNames.SCORPTEC;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.SCORPTEC_RYZEN_5_9600X;
import static com.priceTracker.testingData.cpuData.CPUTestingData.*;
import java.math.BigDecimal;

@Component
public class CPUTestingUtility {

    private final GenericMapper<CPUEntity, CPUDTO> cpuMapper;

    @Autowired
    public CPUTestingUtility(MapperFactory mapperFactory) {
        this.cpuMapper = mapperFactory.create(CPUEntity.class, CPUDTO.class);
    }

    /// SAMPLE ENTITIES/DTOS
    public CPUDTO createTestCPU() {
        return cpuMapper.mapTo(CPUEntity.builder()
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .name(TESTING_CPU_NAME)
                .chipManufacturer(TESTING_CPU_CHIP_MANUFACTURER)
                .series(TESTING_CPU_CHIP_SERIES)
                .cores(TESTING_CPU_CORES)
                .threads(TESTING_CPU_THREADS)
                .baseClock(TESTING_CPU_BASE_CLOCK)
                .boostClock(TESTING_CPU_BOOST_CLOCK)
                .l1Cache(TESTING_CPU_L1_CACHE)
                .l2Cache(TESTING_CPU_L2_CACHE)
                .l3Cache(TESTING_CPU_L3_CACHE)
                .thermalDesignPower(TESTING_CPU_TDP)
                .maxTemperature(TESTING_CPU_MAX_TEMPERATURE)
                .maxMemory(TESTING_CPU_MAX_MEMORY)
                .memorySupported(TESTING_CPU_MEMORY_SUPPORTED)
                .hasIntegratedGPU(TESTING_CPU_INTEGRATED_GPU)
                .isActive(true)
                .build());
    }

    /// SAMPLE PRODUCTS
    public VendorProductDTO createTestScorptecCPU() {
        return VendorProductDTO.builder()
                .productType(PRODUCT_TYPE_CPU)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor(SCORPTEC)
                .url(SCORPTEC_RYZEN_5_9600X)
                .build();
    }

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleCPUPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .price(new BigDecimal("360.00"))
                .build();
    }
}