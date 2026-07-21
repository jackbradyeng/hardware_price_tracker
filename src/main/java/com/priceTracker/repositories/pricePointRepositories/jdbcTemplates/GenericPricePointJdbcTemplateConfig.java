package com.priceTracker.repositories.pricePointRepositories.jdbcTemplates;

import com.priceTracker.domain.entities.pricePointEntities.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.*;

/**
 * Provides the necessary bean definitions to Spring for dependency injection into the respective
 * ProductVendorScrapingOrchestrators. Sequence and table names must come from DatabaseConstants.
 */
@Configuration
public class GenericPricePointJdbcTemplateConfig {

    @Bean
    public GenericPricePointJdbcTemplate<SSDPricePoint> ssdPricePointJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new GenericPricePointJdbcTemplate<>(jdbcTemplate, SSD_PRICE_SEQUENCE, SSD_PRICE_HISTORY);
    }

    @Bean
    public GenericPricePointJdbcTemplate<CPUPricePoint> cpuPricePointJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new GenericPricePointJdbcTemplate<>(jdbcTemplate, CPU_PRICE_SEQUENCE, CPU_PRICE_HISTORY);
    }

    @Bean
    public GenericPricePointJdbcTemplate<GPUPricePoint> gpuPricePointJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new GenericPricePointJdbcTemplate<>(jdbcTemplate, GPU_PRICE_SEQUENCE, GPU_PRICE_HISTORY);
    }

    @Bean
    public GenericPricePointJdbcTemplate<GPUWorkstationPricePoint> gpuWorkstationPricePointJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new GenericPricePointJdbcTemplate<>(jdbcTemplate, WS_GPU_PRICE_SEQUENCE, WORKSTATION_GPU_PRICE_HISTORY);
    }

    @Bean
    public GenericPricePointJdbcTemplate<HDDPricePoint> hddPricePointJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new GenericPricePointJdbcTemplate<>(jdbcTemplate, HDD_PRICE_SEQUENCE, HDD_PRICE_HISTORY);
    }

    @Bean
    public GenericPricePointJdbcTemplate<NVMEPricePoint> nvmePricePointJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new GenericPricePointJdbcTemplate<>(jdbcTemplate, NVME_PRICE_SEQUENCE, NVME_PRICE_HISTORY);
    }

    @Bean
    public GenericPricePointJdbcTemplate<RAMPricePoint> ramPricePointJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new GenericPricePointJdbcTemplate<>(jdbcTemplate, RAM_PRICE_SEQUENCE, RAM_PRICE_HISTORY);
    }
}