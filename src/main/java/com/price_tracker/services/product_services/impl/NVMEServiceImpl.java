package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import com.price_tracker.mappers.product_mappers.NVMEMapper;
import com.price_tracker.repositories.product_repos.NVMERepository;
import com.price_tracker.services.product_services.NVMEService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NVMEServiceImpl implements NVMEService {

    private final NVMERepository nvmeRepository;
    private final NVMEMapper modelMapper;

    @Override
    public NVMEDTO save(NVMEDTO nvmeDTO) {
        NVMEEntity nvmeEntity = modelMapper.mapFrom(nvmeDTO);
        NVMEEntity savedNVMEEntity = nvmeRepository.save(nvmeEntity);
        return modelMapper.mapTo(savedNVMEEntity);
    }

    @Override
    public List<NVMEDTO> saveAll(List<NVMEDTO> nvmeDTOs) {
        List<NVMEEntity> nvmeEntityList = nvmeDTOs.stream()
                .map(modelMapper::mapFrom)
                .toList();

        List<NVMEEntity> savedNVMEEntityList = nvmeRepository.saveAll(nvmeEntityList);

        return savedNVMEEntityList.stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public List<NVMEDTO> findAll() {
        return nvmeRepository.findAll().stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<NVMEDTO> findOne(String id) {
        return nvmeRepository.findById(id).map(modelMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return nvmeRepository.existsById(id);
    }

    @Override
    public Optional<NVMEDTO> fullUpdate(String id, NVMEDTO nvmeDTO) {
        return nvmeRepository.findById(id).map(existing -> {
            NVMEEntity nvmeEntity = modelMapper.mapFrom(nvmeDTO);
            NVMEEntity savedNVMEEntity = nvmeRepository.save(nvmeEntity);
            return modelMapper.mapTo(savedNVMEEntity);
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
            return modelMapper.mapTo(savedNVME);
        });
    }

    @Override
    public void delete(String id) {
        nvmeRepository.deleteById(id);
    }
}