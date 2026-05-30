package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.domain.entities.product_entities.HDDEntity;
import com.price_tracker.mappers.product_mappers.HDDMapper;
import com.price_tracker.repositories.product_repos.HDDRepository;
import com.price_tracker.services.product_services.HDDService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HDDServiceImpl implements HDDService {

    private final HDDRepository hddRepository;
    private final HDDMapper modelMapper;

    @Override
    public HDDDTO save(HDDDTO hddDTO) {
        HDDEntity hddEntity = modelMapper.mapFrom(hddDTO);
        HDDEntity savedHDDEntity = hddRepository.save(hddEntity);
        return modelMapper.mapTo(savedHDDEntity);
    }

    @Override
    public List<HDDDTO> saveAll(List<HDDDTO> hddDTOs) {
        List<HDDEntity> hddEntityList = hddDTOs.stream()
                .map(modelMapper::mapFrom)
                .toList();

        List<HDDEntity> savedHDDEntityList = hddRepository.saveAll(hddEntityList);

        return savedHDDEntityList.stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public List<HDDDTO> findAll() {
        return hddRepository.findAll().stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<HDDDTO> findOne(String id) {
        return hddRepository.findById(id).map(modelMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return hddRepository.existsById(id);
    }

    @Override
    public Optional<HDDDTO> fullUpdate(String id, HDDDTO hddDTO) {
        return hddRepository.findById(id).map(existing -> {
            HDDEntity hddEntity = modelMapper.mapFrom(hddDTO);
            HDDEntity savedHDDEntity = hddRepository.save(hddEntity);
            return modelMapper.mapTo(savedHDDEntity);
        });
    }

    @Override
    public Optional<HDDDTO> partialUpdate(String id, HDDDTO hddDTO) {
        return hddRepository.findById(id).map(existing -> {
            if (hddDTO.getName() != null) existing.setName(hddDTO.getName());
            if (hddDTO.getBrand() != null) existing.setBrand(hddDTO.getBrand());
            if (hddDTO.getCapacity() != null) existing.setCapacity(hddDTO.getCapacity());
            if (hddDTO.getSequentialRead() != null) existing.setSequentialRead(hddDTO.getSequentialRead());
            if (hddDTO.getSequentialWrite() != null) existing.setSequentialWrite(hddDTO.getSequentialWrite());
            if (hddDTO.getMeanTimeBetweenFailures() != null) existing.setMeanTimeBetweenFailures(hddDTO.getMeanTimeBetweenFailures());
            if (hddDTO.getStorageInterface() != null) existing.setStorageInterface(hddDTO.getStorageInterface());
            if (hddDTO.getFormFactor() != null) existing.setFormFactor(hddDTO.getFormFactor());
            if (hddDTO.getRpm() != null) existing.setRpm(hddDTO.getRpm());
            if (hddDTO.getCache() != null) existing.setCache(hddDTO.getCache());
            if (hddDTO.getIsActive() != null) existing.setIsActive(hddDTO.getIsActive());
            HDDEntity savedHDD = hddRepository.save(existing);
            return modelMapper.mapTo(savedHDD);
        });
    }

    @Override
    public void delete(String id) {
        hddRepository.deleteById(id);
    }
}