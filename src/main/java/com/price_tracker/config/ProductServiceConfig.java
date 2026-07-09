package com.price_tracker.config;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.domain.entities.product_entities.HDDEntity;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.CPURepository;
import com.price_tracker.repositories.product_repos.GPURepository;
import com.price_tracker.repositories.product_repos.GPUWorkstationRepository;
import com.price_tracker.repositories.product_repos.HDDRepository;
import com.price_tracker.repositories.product_repos.NVMERepository;
import com.price_tracker.repositories.product_repos.RAMRepository;
import com.price_tracker.repositories.product_repos.SSDRepository;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.services.product_services.impl.GenericProductServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductServiceConfig {

    @Bean
    public GenericProductService<CPUDTO> cpuService(CPURepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<CPUEntity, CPUDTO>(repository,
                mapperFactory.create(CPUEntity.class, CPUDTO.class), nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<GPUDTO> gpuService(GPURepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<GPUEntity, GPUDTO>(repository,
                mapperFactory.create(GPUEntity.class, GPUDTO.class), nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<GPUWorkstationDTO> gpuWorkstationService(GPUWorkstationRepository repository,
                                                                          MapperFactory mapperFactory,
                                                                          @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<GPUWorkstationEntity, GPUWorkstationDTO>(repository,
                mapperFactory.create(GPUWorkstationEntity.class, GPUWorkstationDTO.class), nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<HDDDTO> hddService(HDDRepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<HDDEntity, HDDDTO>(repository,
                mapperFactory.create(HDDEntity.class, HDDDTO.class), nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<NVMEDTO> nvmeService(NVMERepository repository, MapperFactory mapperFactory,
                                                      @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<NVMEEntity, NVMEDTO>(repository,
                mapperFactory.create(NVMEEntity.class, NVMEDTO.class), nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<RAMDTO> ramService(RAMRepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<RAMEntity, RAMDTO>(repository,
                mapperFactory.create(RAMEntity.class, RAMDTO.class), nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<SSDDTO> ssdService(SSDRepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<SSDEntity, SSDDTO>(repository,
                mapperFactory.create(SSDEntity.class, SSDDTO.class), nullSafeModelMapper);
    }
}