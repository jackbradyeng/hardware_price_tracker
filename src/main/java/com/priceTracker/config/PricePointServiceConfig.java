package com.priceTracker.config;

import com.priceTracker.domain.dto.hybridDTOs.*;
import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.dto.productDTOs.*;
import com.priceTracker.domain.entities.pricePointEntities.*;
import com.priceTracker.domain.entities.productEntities.*;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.pricePointRepositories.*;
import com.priceTracker.repositories.pricePointRepositories.jdbcTemplates.GenericPricePointJdbcTemplate;
import com.priceTracker.repositories.scrapingJobRepositories.ScrapingJobResultRepository;
import com.priceTracker.services.pricePointServices.GenericPricePointService;
import com.priceTracker.services.pricePointServices.impl.GenericPricePointServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A set of bean definitions for each of the respective price point service implementations.
 */
@Configuration
public class PricePointServiceConfig {

    @Bean
    public GenericPricePointService<CPUDataAndPricePointDTO> cpuPricePointService(
            CPUPricePointRepository repository,
            ScrapingJobResultRepository scrapingJobResultRepository,
            GenericPricePointJdbcTemplate<CPUPricePoint> pricePointJdbcTemplate,
            MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
                scrapingJobResultRepository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(CPUPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(CPUEntity.class, CPUDTO.class),
                pricePointJdbcTemplate,
                (productDTO, pricePoints, page, pageSize, totalPages, totalElements)
                        -> CPUDataAndPricePointDTO.builder()
                        .cpuDTO(productDTO)
                        .cpuPricePointDTOList(pricePoints)
                        .page(page)
                        .pageSize(pageSize)
                        .totalPages(totalPages)
                        .totalElements(totalElements)
                        .build());
    }

    @Bean
    public GenericPricePointService<GPUDataAndPricePointDTO> gpuPricePointService(
            GPUPricePointRepository repository,
            ScrapingJobResultRepository scrapingJobResultRepository,
            GenericPricePointJdbcTemplate<GPUPricePoint> pricePointJdbcTemplate,
            MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
                scrapingJobResultRepository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(GPUPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(GPUEntity.class, GPUDTO.class),
                pricePointJdbcTemplate,
                (productDTO, pricePoints, page, pageSize, totalPages, totalElements)
                        -> GPUDataAndPricePointDTO.builder()
                        .gpuDTO(productDTO)
                        .gpuPricePointDTOList(pricePoints)
                        .page(page)
                        .pageSize(pageSize)
                        .totalPages(totalPages)
                        .totalElements(totalElements)
                        .build());
    }

    @Bean
    public GenericPricePointService<GPUWorkstationDataAndPricePointDTO> gpuWorkstationPricePointService(
            GPUWorkstationPricePointRepository repository,
            ScrapingJobResultRepository scrapingJobResultRepository,
            GenericPricePointJdbcTemplate<GPUWorkstationPricePoint> pricePointJdbcTemplate,
            MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
                scrapingJobResultRepository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(GPUWorkstationPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(GPUWorkstationEntity.class, GPUWorkstationDTO.class),
                pricePointJdbcTemplate,
                (productDTO, pricePoints, page, pageSize, totalPages, totalElements)
                        -> GPUWorkstationDataAndPricePointDTO.builder()
                        .gpuWorkstationDTO(productDTO)
                        .gpuWorkstationPricePointDTOList(pricePoints)
                        .page(page)
                        .pageSize(pageSize)
                        .totalPages(totalPages)
                        .totalElements(totalElements)
                        .build());
    }

    @Bean
    public GenericPricePointService<HDDDataAndPricePointDTO> hddPricePointService(
            HDDPricePointRepository repository,
            ScrapingJobResultRepository scrapingJobResultRepository,
            GenericPricePointJdbcTemplate<HDDPricePoint> pricePointJdbcTemplate,
            MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
                scrapingJobResultRepository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(HDDPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(HDDEntity.class, HDDDTO.class),
                pricePointJdbcTemplate,
                (productDTO, pricePoints, page, pageSize, totalPages, totalElements)
                        -> HDDDataAndPricePointDTO.builder()
                        .hddDTO(productDTO)
                        .hddPricePointDTOList(pricePoints)
                        .page(page)
                        .pageSize(pageSize)
                        .totalPages(totalPages)
                        .totalElements(totalElements)
                        .build());
    }

    @Bean
    public GenericPricePointService<NVMEDataAndPricePointDTO> nvmePricePointService(
            NVMEPricePointRepository repository,
            ScrapingJobResultRepository scrapingJobResultRepository,
            GenericPricePointJdbcTemplate<NVMEPricePoint> pricePointJdbcTemplate,
            MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
                scrapingJobResultRepository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(NVMEPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(NVMEEntity.class, NVMEDTO.class),
                pricePointJdbcTemplate,
                (productDTO, pricePoints, page, pageSize, totalPages, totalElements)
                        -> NVMEDataAndPricePointDTO.builder()
                        .nvmeDTO(productDTO)
                        .nvmePricePointDTOList(pricePoints)
                        .page(page)
                        .pageSize(pageSize)
                        .totalPages(totalPages)
                        .totalElements(totalElements)
                        .build());
    }

    @Bean
    public GenericPricePointService<RAMDataAndPricePointDTO> ramPricePointService(
            RAMPricePointRepository repository,
            ScrapingJobResultRepository scrapingJobResultRepository,
            GenericPricePointJdbcTemplate<RAMPricePoint> pricePointJdbcTemplate,
            MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
                scrapingJobResultRepository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(RAMPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(RAMEntity.class, RAMDTO.class),
                pricePointJdbcTemplate,
                (productDTO, pricePoints, page, pageSize, totalPages, totalElements)
                        -> RAMDataAndPricePointDTO.builder()
                        .ramDTO(productDTO)
                        .ramPricePointDTOList(pricePoints)
                        .page(page)
                        .pageSize(pageSize)
                        .totalPages(totalPages)
                        .totalElements(totalElements)
                        .build());
    }

    @Bean
    public GenericPricePointService<SSDDataAndPricePointDTO> ssdPricePointService(
            SSDPricePointRepository repository,
            ScrapingJobResultRepository scrapingJobResultRepository,
            GenericPricePointJdbcTemplate<SSDPricePoint> pricePointJdbcTemplate,
            MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
                scrapingJobResultRepository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(SSDPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(SSDEntity.class, SSDDTO.class),
                pricePointJdbcTemplate,
                (productDTO, pricePoints, page, pageSize, totalPages, totalElements)
                        -> SSDDataAndPricePointDTO.builder()
                        .ssdDTO(productDTO)
                        .ssdPricePointDTOList(pricePoints)
                        .page(page)
                        .pageSize(pageSize)
                        .totalPages(totalPages)
                        .totalElements(totalElements)
                        .build());
    }
}