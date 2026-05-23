package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
import com.price_tracker.mappers.product_mappers.SSDMapper;
import com.price_tracker.repositories.product_repos.SSDRepository;
import com.price_tracker.services.product_services.SSDService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SSDServiceImpl implements SSDService {

    private final SSDRepository ssdRepository;
    private final SSDMapper modelMapper;

    @Override
    public SSDDTO save(SSDDTO ssdDTO) {
        SSDEntity ssdEntity = modelMapper.mapFrom(ssdDTO);
        SSDEntity savedSSDEntity = ssdRepository.save(ssdEntity);
        return modelMapper.mapTo(savedSSDEntity);
    }

    @Override
    public List<SSDDTO> saveAll(List<SSDDTO> ssdDTOs) {
        List<SSDEntity> ssdEntityList = ssdDTOs.stream()
                .map(modelMapper::mapFrom)
                .toList();

        List<SSDEntity> savedSSDEntityList = ssdRepository.saveAll(ssdEntityList);

        return savedSSDEntityList.stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public List<SSDDTO> findAll() {
        return ssdRepository.findAll().stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<SSDDTO> findOne(String id) {
        return ssdRepository.findById(id).map(modelMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return ssdRepository.existsById(id);
    }

    @Override
    public Optional<SSDDTO> fullUpdate(String id, SSDDTO ssdDTO) {
        return ssdRepository.findById(id).map(existing -> {
            SSDEntity ssdEntity = modelMapper.mapFrom(ssdDTO);
            SSDEntity savedSSDEntity = ssdRepository.save(ssdEntity);
            return modelMapper.mapTo(savedSSDEntity);
        });
    }

    @Override
    public Optional<SSDDTO> partialUpdate(String id, SSDDTO ssdDTO) {
        return ssdRepository.findById(id).map(existing -> {
            if (ssdDTO.getName() != null) existing.setName(ssdDTO.getName());
            if (ssdDTO.getBrand() != null) existing.setBrand(ssdDTO.getBrand());
            if (ssdDTO.getCapacity() != null) existing.setCapacity(ssdDTO.getCapacity());
            if (ssdDTO.getSequentialRead() != null) existing.setSequentialRead(ssdDTO.getSequentialRead());
            if (ssdDTO.getSequentialWrite() != null) existing.setSequentialWrite(ssdDTO.getSequentialWrite());
            if (ssdDTO.getMeanTimeBetweenFailures() != null) existing.setMeanTimeBetweenFailures(ssdDTO.getMeanTimeBetweenFailures());
            if (ssdDTO.getStorageInterface() != null) existing.setStorageInterface(ssdDTO.getStorageInterface());
            SSDEntity savedSSD = ssdRepository.save(existing);
            return modelMapper.mapTo(savedSSD);
        });
    }

    @Override
    public void delete(String id) {
        ssdRepository.deleteById(id);
    }
}