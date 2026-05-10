package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.mappers.product_mappers.RAMMapper;
import com.price_tracker.repositories.product_repos.RAMRepository;
import com.price_tracker.services.product_services.RAMService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RAMServiceImpl implements RAMService {

    private final RAMRepository ramRepository;
    private final RAMMapper modelMapper;

    @Override
    public RAMDTO save(RAMDTO ramDTO) {
        RAMEntity ramEntity = modelMapper.mapFrom(ramDTO);
        RAMEntity savedRAMEntity = ramRepository.save(ramEntity);
        return modelMapper.mapTo(savedRAMEntity);
    }

    @Override
    public List<RAMDTO> saveAll(List<RAMDTO> ramDTOs) {
        List<RAMEntity> ramEntityList = ramDTOs.stream()
                .map(modelMapper::mapFrom)
                .toList();

        List<RAMEntity> savedRAMEntityList = ramRepository.saveAll(ramEntityList);

        return savedRAMEntityList.stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public List<RAMDTO> findAll() {
        return ramRepository.findAll().stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<RAMDTO> findOne(String id) {
        return ramRepository.findById(id).map(modelMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return ramRepository.existsById(id);
    }

    @Override
    public Optional<RAMDTO> fullUpdate(String id, RAMDTO ramDTO) {
        return ramRepository.findById(id).map(existing -> {
            RAMEntity ramEntity = modelMapper.mapFrom(ramDTO);
            RAMEntity savedRAMEntity = ramRepository.save(ramEntity);
            return modelMapper.mapTo(savedRAMEntity);
        });
    }

    @Override
    public Optional<RAMDTO> partialUpdate(String id, RAMDTO ramDTO) {
        return ramRepository.findById(id).map(existing -> {
            if (ramDTO.getName() != null) existing.setName(ramDTO.getName());
            RAMEntity savedRAM = ramRepository.save(existing);
            return modelMapper.mapTo(savedRAM);
        });
    }

    @Override
    public void delete(String id) {
        ramRepository.deleteById(id);
    }
}