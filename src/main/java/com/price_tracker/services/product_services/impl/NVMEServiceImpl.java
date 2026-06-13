package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.NVMERepository;
import com.price_tracker.services.product_services.NVMEService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NVMEServiceImpl implements NVMEService {

    private final NVMERepository nvmeRepository;
    private final GenericMapper<NVMEEntity, NVMEDTO> nvmeMapper;

    @Autowired
    public NVMEServiceImpl(NVMERepository nvmeRepository, MapperFactory mapperFactory) {
        this.nvmeRepository = nvmeRepository;
        this.nvmeMapper = mapperFactory.create(NVMEEntity.class, NVMEDTO.class);
    }

    @Override
    public NVMEDTO save(NVMEDTO nvmeDTO) {
        NVMEEntity nvmeEntity = nvmeMapper.mapFrom(nvmeDTO);
        NVMEEntity savedNVMEEntity = nvmeRepository.save(nvmeEntity);
        return nvmeMapper.mapTo(savedNVMEEntity);
    }

    @Override
    public List<NVMEDTO> saveAll(List<NVMEDTO> nvmeDTOs) {
        List<NVMEEntity> nvmeEntityList = nvmeDTOs.stream()
                .map(nvmeMapper::mapFrom)
                .toList();

        List<NVMEEntity> savedNVMEEntityList = nvmeRepository.saveAll(nvmeEntityList);

        return savedNVMEEntityList.stream()
                .map(nvmeMapper::mapTo)
                .toList();
    }

    @Override
    public List<NVMEDTO> findAll() {
        return nvmeRepository.findAll().stream()
                .map(nvmeMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<NVMEDTO> findOne(String id) {
        return nvmeRepository.findById(id).map(nvmeMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return nvmeRepository.existsById(id);
    }

    @Override
    public Optional<NVMEDTO> fullUpdate(String id, NVMEDTO nvmeDTO) {
        return nvmeRepository.findById(id).map(existing -> {
            NVMEEntity nvmeEntity = nvmeMapper.mapFrom(nvmeDTO);
            NVMEEntity savedNVMEEntity = nvmeRepository.save(nvmeEntity);
            return nvmeMapper.mapTo(savedNVMEEntity);
        });
    }

    @Override
    public Optional<NVMEDTO> partialUpdate(String id, NVMEDTO nvmeDTO) {
        return nvmeRepository.findById(id).map(existing -> {
            if (nvmeDTO.getName() != null) existing.setName(nvmeDTO.getName());
            if (nvmeDTO.getBrand() != null) existing.setBrand(nvmeDTO.getBrand());
            if (nvmeDTO.getCapacity() != null) existing.setCapacity(nvmeDTO.getCapacity());
            if (nvmeDTO.getSequentialRead() != null) existing.setSequentialRead(nvmeDTO.getSequentialRead());
            if (nvmeDTO.getSequentialWrite() != null) existing.setSequentialWrite(nvmeDTO.getSequentialWrite());
            if (nvmeDTO.getMeanTimeBetweenFailures() != null) existing.setMeanTimeBetweenFailures(nvmeDTO.getMeanTimeBetweenFailures());
            if (nvmeDTO.getStorageInterface() != null) existing.setStorageInterface(nvmeDTO.getStorageInterface());
            if (nvmeDTO.getIncludesHeatSink() != null) existing.setIncludesHeatSink(nvmeDTO.getIncludesHeatSink());
            if (nvmeDTO.getIsActive() != null) existing.setIsActive(nvmeDTO.getIsActive());
            NVMEEntity savedNVME = nvmeRepository.save(existing);
            return nvmeMapper.mapTo(savedNVME);
        });
    }

    @Override
    public void delete(String id) {
        nvmeRepository.deleteById(id);
    }
}