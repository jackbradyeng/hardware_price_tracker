package com.priceTracker.config;

import com.priceTracker.domain.dto.hybridDTOs.CPUDataAndPricePointDTO;
import com.priceTracker.domain.dto.hybridDTOs.GPUDataAndPricePointDTO;
import com.priceTracker.domain.dto.hybridDTOs.GPUWorkstationDataAndPricePointDTO;
import com.priceTracker.domain.dto.hybridDTOs.HDDDataAndPricePointDTO;
import com.priceTracker.domain.dto.hybridDTOs.NVMEDataAndPricePointDTO;
import com.priceTracker.domain.dto.hybridDTOs.RAMDataAndPricePointDTO;
import com.priceTracker.domain.dto.hybridDTOs.SSDDataAndPricePointDTO;
import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.dto.productDTOs.CPUDTO;
import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.domain.dto.productDTOs.GPUWorkstationDTO;
import com.priceTracker.domain.dto.productDTOs.HDDDTO;
import com.priceTracker.domain.dto.productDTOs.NVMEDTO;
import com.priceTracker.domain.dto.productDTOs.RAMDTO;
import com.priceTracker.domain.dto.productDTOs.SSDDTO;
import com.priceTracker.domain.entities.pricePointEntities.CPUPricePoint;
import com.priceTracker.domain.entities.pricePointEntities.GPUPricePoint;
import com.priceTracker.domain.entities.pricePointEntities.GPUWorkstationPricePoint;
import com.priceTracker.domain.entities.pricePointEntities.HDDPricePoint;
import com.priceTracker.domain.entities.pricePointEntities.NVMEPricePoint;
import com.priceTracker.domain.entities.pricePointEntities.RAMPricePoint;
import com.priceTracker.domain.entities.pricePointEntities.SSDPricePoint;
import com.priceTracker.domain.entities.productEntities.CPUEntity;
import com.priceTracker.domain.entities.productEntities.GPUEntity;
import com.priceTracker.domain.entities.productEntities.GPUWorkstationEntity;
import com.priceTracker.domain.entities.productEntities.HDDEntity;
import com.priceTracker.domain.entities.productEntities.NVMEEntity;
import com.priceTracker.domain.entities.productEntities.RAMEntity;
import com.priceTracker.domain.entities.productEntities.SSDEntity;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.pricePointRepositories.CPUPricePointRepository;
import com.priceTracker.repositories.pricePointRepositories.GPUPricePointRepository;
import com.priceTracker.repositories.pricePointRepositories.GPUWorkstationPricePointRepository;
import com.priceTracker.repositories.pricePointRepositories.HDDPricePointRepository;
import com.priceTracker.repositories.pricePointRepositories.NVMEPricePointRepository;
import com.priceTracker.repositories.pricePointRepositories.RAMPricePointRepository;
import com.priceTracker.repositories.pricePointRepositories.SSDPricePointRepository;
import com.priceTracker.repositories.pricePointRepositories.jdbcTemplates.GenericPricePointJdbcTemplate;
import com.priceTracker.services.pricePointServices.GenericPricePointService;
import com.priceTracker.services.pricePointServices.impl.GenericPricePointServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PricePointServiceConfig {

    @Bean
    public GenericPricePointService<CPUDataAndPricePointDTO>
    cpuPricePointService(CPUPricePointRepository repository,
                         GenericPricePointJdbcTemplate<CPUPricePoint> pricePointJdbcTemplate,
                         MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
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
    public GenericPricePointService<GPUDataAndPricePointDTO>
    gpuPricePointService(GPUPricePointRepository repository,
                         GenericPricePointJdbcTemplate<GPUPricePoint> pricePointJdbcTemplate,
                         MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
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
    public GenericPricePointService<GPUWorkstationDataAndPricePointDTO>
    gpuWorkstationPricePointService(GPUWorkstationPricePointRepository repository,
                                    GenericPricePointJdbcTemplate<GPUWorkstationPricePoint> pricePointJdbcTemplate,
                                    MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
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
    public GenericPricePointService<HDDDataAndPricePointDTO>
    hddPricePointService(HDDPricePointRepository repository,
                         GenericPricePointJdbcTemplate<HDDPricePoint> pricePointJdbcTemplate,
                         MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
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
    public GenericPricePointService<NVMEDataAndPricePointDTO>
    nvmePricePointService(NVMEPricePointRepository repository,
                          GenericPricePointJdbcTemplate<NVMEPricePoint> pricePointJdbcTemplate,
                          MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
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
    public GenericPricePointService<RAMDataAndPricePointDTO>
    ramPricePointService(RAMPricePointRepository repository,
                         GenericPricePointJdbcTemplate<RAMPricePoint> pricePointJdbcTemplate,
                         MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
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
    public GenericPricePointService<SSDDataAndPricePointDTO>
    ssdPricePointService(SSDPricePointRepository repository,
                         GenericPricePointJdbcTemplate<SSDPricePoint> pricePointJdbcTemplate,
                         MapperFactory mapperFactory) {

        return new GenericPricePointServiceImpl<>(
                repository,
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