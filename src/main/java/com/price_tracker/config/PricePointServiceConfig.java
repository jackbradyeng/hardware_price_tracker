package com.price_tracker.config;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_dtos.GPUWorkstationDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_dtos.HDDDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_dtos.NVMEDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.hybrid_dtos.SSDDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.domain.entities.price_point_entities.GPUWorkstationPricePoint;
import com.price_tracker.domain.entities.price_point_entities.HDDPricePoint;
import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.domain.entities.product_entities.HDDEntity;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.CPUPricePointRepository;
import com.price_tracker.repositories.price_point_repos.GPUPricePointRepository;
import com.price_tracker.repositories.price_point_repos.GPUWorkstationPricePointRepository;
import com.price_tracker.repositories.price_point_repos.HDDPricePointRepository;
import com.price_tracker.repositories.price_point_repos.NVMEPricePointRepository;
import com.price_tracker.repositories.price_point_repos.RAMPricePointRepository;
import com.price_tracker.repositories.price_point_repos.SSDPricePointRepository;
import com.price_tracker.services.price_point_services.GenericPricePointService;
import com.price_tracker.services.price_point_services.impl.GenericPricePointServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PricePointServiceConfig {

    @Bean
    public GenericPricePointService<CPUDataAndPricePointDTO> cpuPricePointService(CPUPricePointRepository repository,
                                                                                  MapperFactory mapperFactory) {
        return new GenericPricePointServiceImpl<CPUEntity, CPUPricePoint, CPUDTO, CPUDataAndPricePointDTO>(
                repository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(CPUPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(CPUEntity.class, CPUDTO.class),
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
    public GenericPricePointService<GPUDataAndPricePointDTO> gpuPricePointService(GPUPricePointRepository repository,
                                                                                  MapperFactory mapperFactory) {
        return new GenericPricePointServiceImpl<GPUEntity, GPUPricePoint, GPUDTO, GPUDataAndPricePointDTO>(
                repository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(GPUPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(GPUEntity.class, GPUDTO.class),
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
            GPUWorkstationPricePointRepository repository, MapperFactory mapperFactory) {
        return new GenericPricePointServiceImpl<GPUWorkstationEntity, GPUWorkstationPricePoint, GPUWorkstationDTO, GPUWorkstationDataAndPricePointDTO>(
                repository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(GPUWorkstationPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(GPUWorkstationEntity.class, GPUWorkstationDTO.class),
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
    public GenericPricePointService<HDDDataAndPricePointDTO> hddPricePointService(HDDPricePointRepository repository,
                                                                                  MapperFactory mapperFactory) {
        return new GenericPricePointServiceImpl<HDDEntity, HDDPricePoint, HDDDTO, HDDDataAndPricePointDTO>(
                repository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(HDDPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(HDDEntity.class, HDDDTO.class),
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
    public GenericPricePointService<NVMEDataAndPricePointDTO> nvmePricePointService(NVMEPricePointRepository repository,
                                                                                    MapperFactory mapperFactory) {
        return new GenericPricePointServiceImpl<NVMEEntity, NVMEPricePoint, NVMEDTO, NVMEDataAndPricePointDTO>(
                repository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(NVMEPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(NVMEEntity.class, NVMEDTO.class),
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
    public GenericPricePointService<RAMDataAndPricePointDTO> ramPricePointService(RAMPricePointRepository repository,
                                                                                  MapperFactory mapperFactory) {
        return new GenericPricePointServiceImpl<RAMEntity, RAMPricePoint, RAMDTO, RAMDataAndPricePointDTO>(
                repository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(RAMPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(RAMEntity.class, RAMDTO.class),
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
    public GenericPricePointService<SSDDataAndPricePointDTO> ssdPricePointService(SSDPricePointRepository repository,
                                                                                  MapperFactory mapperFactory) {
        return new GenericPricePointServiceImpl<SSDEntity, SSDPricePoint, SSDDTO, SSDDataAndPricePointDTO>(
                repository,
                repository::getPricePointsByModelNumber,
                mapperFactory.create(SSDPricePoint.class, GenericPricePointDTO.class),
                mapperFactory.create(SSDEntity.class, SSDDTO.class),
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