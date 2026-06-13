package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.RAMRepository;
import com.price_tracker.services.product_services.RAMService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RAMServiceImpl implements RAMService {

    private final RAMRepository ramRepository;
    private final GenericMapper<RAMEntity, RAMDTO> ramMapper;

    @Autowired
    public RAMServiceImpl(RAMRepository ramRepository, MapperFactory mapperFactory) {
        this.ramRepository = ramRepository;
        this.ramMapper = mapperFactory.create(RAMEntity.class, RAMDTO.class);
    }

    @Override
    public RAMDTO save(RAMDTO ramDTO) {
        RAMEntity ramEntity = ramMapper.mapFrom(ramDTO);
        RAMEntity savedRAMEntity = ramRepository.save(ramEntity);
        return ramMapper.mapTo(savedRAMEntity);
    }

    @Override
    public List<RAMDTO> saveAll(List<RAMDTO> ramDTOs) {
        List<RAMEntity> ramEntityList = ramDTOs.stream()
                .map(ramMapper::mapFrom)
                .toList();

        List<RAMEntity> savedRAMEntityList = ramRepository.saveAll(ramEntityList);

        return savedRAMEntityList.stream()
                .map(ramMapper::mapTo)
                .toList();
    }

    @Override
    public List<RAMDTO> findAll() {
        return ramRepository.findAll().stream()
                .map(ramMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<RAMDTO> findOne(String id) {
        return ramRepository.findById(id).map(ramMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return ramRepository.existsById(id);
    }

    @Override
    public Optional<RAMDTO> fullUpdate(String id, RAMDTO ramDTO) {
        return ramRepository.findById(id).map(existing -> {
            RAMEntity ramEntity = ramMapper.mapFrom(ramDTO);
            RAMEntity savedRAMEntity = ramRepository.save(ramEntity);
            return ramMapper.mapTo(savedRAMEntity);
        });
    }

    @Override
    public Optional<RAMDTO> partialUpdate(String id, RAMDTO ramDTO) {
        return ramRepository.findById(id).map(existing -> {
            if (ramDTO.getName() != null) existing.setName(ramDTO.getName());
            RAMEntity savedRAM = ramRepository.save(existing);
            return ramMapper.mapTo(savedRAM);
        });
    }

    @Override
    public void delete(String id) {
        ramRepository.deleteById(id);
    }
}