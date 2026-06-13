package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.SSDRepository;
import com.price_tracker.services.product_services.SSDService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SSDServiceImpl implements SSDService {

    private final SSDRepository ssdRepository;
    private final GenericMapper<SSDEntity, SSDDTO> ssdMapper;

    @Autowired
    public SSDServiceImpl(SSDRepository ssdRepository, MapperFactory mapperFactory) {
        this.ssdRepository = ssdRepository;
        this.ssdMapper = mapperFactory.create(SSDEntity.class, SSDDTO.class);
    }

    @Override
    public SSDDTO save(SSDDTO ssdDTO) {
        SSDEntity ssdEntity = ssdMapper.mapFrom(ssdDTO);
        SSDEntity savedSSDEntity = ssdRepository.save(ssdEntity);
        return ssdMapper.mapTo(savedSSDEntity);
    }

    @Override
    public List<SSDDTO> saveAll(List<SSDDTO> ssdDTOs) {
        List<SSDEntity> ssdEntityList = ssdDTOs.stream()
                .map(ssdMapper::mapFrom)
                .toList();

        List<SSDEntity> savedSSDEntityList = ssdRepository.saveAll(ssdEntityList);

        return savedSSDEntityList.stream()
                .map(ssdMapper::mapTo)
                .toList();
    }

    @Override
    public List<SSDDTO> findAll() {
        return ssdRepository.findAll().stream()
                .map(ssdMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<SSDDTO> findOne(String id) {
        return ssdRepository.findById(id).map(ssdMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return ssdRepository.existsById(id);
    }

    @Override
    public Optional<SSDDTO> fullUpdate(String id, SSDDTO ssdDTO) {
        return ssdRepository.findById(id).map(existing -> {
            SSDEntity ssdEntity = ssdMapper.mapFrom(ssdDTO);
            SSDEntity savedSSDEntity = ssdRepository.save(ssdEntity);
            return ssdMapper.mapTo(savedSSDEntity);
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
            if (ssdDTO.getIsActive() != null) existing.setIsActive(ssdDTO.getIsActive());
            SSDEntity savedSSD = ssdRepository.save(existing);
            return ssdMapper.mapTo(savedSSD);
        });
    }

    @Override
    public void delete(String id) {
        ssdRepository.deleteById(id);
    }
}