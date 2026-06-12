package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.GPURepository;
import com.price_tracker.services.product_services.GPUService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GPUServiceImpl implements GPUService {

    private final GPURepository gpuRepository;
    private final GenericMapper<GPUEntity, GPUDTO> gpuMapper;

    @Autowired
    public GPUServiceImpl(GPURepository gpuRepository, MapperFactory mapperFactory) {
        this.gpuRepository = gpuRepository;
        this.gpuMapper = mapperFactory.create(GPUEntity.class, GPUDTO.class);
    }

    @Override
    public GPUDTO save(GPUDTO gpuDTO) {
        GPUEntity gpuEntity = gpuMapper.mapFrom(gpuDTO);
        GPUEntity savedGPUEntity = gpuRepository.save(gpuEntity);
        return gpuMapper.mapTo(savedGPUEntity);
    }

    @Override
    public List<GPUDTO> saveAll(List<GPUDTO> gpuDTOs) {
        List<GPUEntity> gpuEntityList = gpuDTOs.stream()
                .map(gpuMapper::mapFrom)
                .toList();

        List<GPUEntity> savedGPUEntityList = gpuRepository.saveAll(gpuEntityList);

        return savedGPUEntityList.stream()
                .map(gpuMapper::mapTo)
                .toList();
    }

    @Override
    public List<GPUDTO> findAll() {
        return gpuRepository.findAll().stream()
                .map(gpuMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<GPUDTO> findOne(String id) {
        return gpuRepository.findById(id).map(gpuMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return gpuRepository.existsById(id);
    }

    @Override
    public Optional<GPUDTO> fullUpdate(String id, GPUDTO gpuDTO) {
        return gpuRepository.findById(id).map(existing -> {
            GPUEntity gpuEntity = gpuMapper.mapFrom(gpuDTO);
            GPUEntity savedGPUEntity = gpuRepository.save(gpuEntity);
            return gpuMapper.mapTo(savedGPUEntity);
        });
    }

    @Override
    public Optional<GPUDTO> partialUpdate(String id, GPUDTO gpuDTO) {
        return gpuRepository.findById(id).map(existing -> {
            if (gpuDTO.getName() != null) existing.setName(gpuDTO.getName());
            GPUEntity savedGPU = gpuRepository.save(existing);
            return gpuMapper.mapTo(savedGPU);
        });
    }

    @Override
    public void delete(String id) {
        gpuRepository.deleteById(id);
    }
}