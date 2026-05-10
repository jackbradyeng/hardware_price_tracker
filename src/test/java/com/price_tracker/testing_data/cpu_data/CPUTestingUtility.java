package com.price_tracker.testing_data.cpu_data;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.mappers.product_mappers.CPUMapper;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import static com.price_tracker.testing_data.cpu_data.CPUTestingData.*;
import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class CPUTestingUtility {

    private final CPUMapper cpuMapper;

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

    /// SAMPLE PRICE POINTS
    public ScrapedDataDTO createSampleCPUPricePointData() {
        return ScrapedDataDTO.builder()
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .price(new BigDecimal(TESTING_CPU_PRICE))
                .build();
    }
}