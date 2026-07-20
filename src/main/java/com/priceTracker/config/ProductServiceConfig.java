package com.priceTracker.config;

import com.priceTracker.domain.dto.productDTOs.CPUDTO;
import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.domain.dto.productDTOs.GPUWorkstationDTO;
import com.priceTracker.domain.dto.productDTOs.HDDDTO;
import com.priceTracker.domain.dto.productDTOs.NVMEDTO;
import com.priceTracker.domain.dto.productDTOs.RAMDTO;
import com.priceTracker.domain.dto.productDTOs.SSDDTO;
import com.priceTracker.domain.entities.productEntities.CPUEntity;
import com.priceTracker.domain.entities.productEntities.GPUEntity;
import com.priceTracker.domain.entities.productEntities.GPUWorkstationEntity;
import com.priceTracker.domain.entities.productEntities.HDDEntity;
import com.priceTracker.domain.entities.productEntities.NVMEEntity;
import com.priceTracker.domain.entities.productEntities.RAMEntity;
import com.priceTracker.domain.entities.productEntities.SSDEntity;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.productRepositories.CPURepository;
import com.priceTracker.repositories.productRepositories.GPURepository;
import com.priceTracker.repositories.productRepositories.GPUWorkstationRepository;
import com.priceTracker.repositories.productRepositories.HDDRepository;
import com.priceTracker.repositories.productRepositories.NVMERepository;
import com.priceTracker.repositories.productRepositories.RAMRepository;
import com.priceTracker.repositories.productRepositories.SSDRepository;
import com.priceTracker.services.productServices.GenericProductService;
import com.priceTracker.services.productServices.impl.GenericProductServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductServiceConfig {

    @Bean
    public GenericProductService<CPUDTO> cpuService(CPURepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("modelMapper") ModelMapper modelMapper,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<>(repository,
                mapperFactory.create(CPUEntity.class, CPUDTO.class), modelMapper, nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<GPUDTO> gpuService(GPURepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("modelMapper") ModelMapper modelMapper,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<>(repository,
                mapperFactory.create(GPUEntity.class, GPUDTO.class), modelMapper, nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<GPUWorkstationDTO> gpuWorkstationService(GPUWorkstationRepository repository,
                                                                          MapperFactory mapperFactory,
                                                                          @Qualifier("modelMapper") ModelMapper modelMapper,
                                                                          @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<>(repository,
                mapperFactory.create(GPUWorkstationEntity.class, GPUWorkstationDTO.class), modelMapper, nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<HDDDTO> hddService(HDDRepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("modelMapper") ModelMapper modelMapper,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<>(repository,
                mapperFactory.create(HDDEntity.class, HDDDTO.class), modelMapper, nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<NVMEDTO> nvmeService(NVMERepository repository, MapperFactory mapperFactory,
                                                      @Qualifier("modelMapper") ModelMapper modelMapper,
                                                      @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<>(repository,
                mapperFactory.create(NVMEEntity.class, NVMEDTO.class), modelMapper, nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<RAMDTO> ramService(RAMRepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("modelMapper") ModelMapper modelMapper,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<>(repository,
                mapperFactory.create(RAMEntity.class, RAMDTO.class), modelMapper, nullSafeModelMapper);
    }

    @Bean
    public GenericProductService<SSDDTO> ssdService(SSDRepository repository, MapperFactory mapperFactory,
                                                    @Qualifier("modelMapper") ModelMapper modelMapper,
                                                    @Qualifier("nullSafeModelMapper") ModelMapper nullSafeModelMapper) {
        return new GenericProductServiceImpl<>(repository,
                mapperFactory.create(SSDEntity.class, SSDDTO.class), modelMapper, nullSafeModelMapper);
    }
}