package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.CPURepository;
import com.price_tracker.services.product_services.CPUService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CPUServiceImpl implements CPUService {

    private final CPURepository cpuRepository;
    private final GenericMapper<CPUEntity, CPUDTO> cpuMapper;

    @Autowired
    public CPUServiceImpl(CPURepository cpuRepository, MapperFactory mapperFactory) {
        this.cpuRepository = cpuRepository;
        this.cpuMapper = mapperFactory.create(CPUEntity.class, CPUDTO.class);
    }

    @Override
    public CPUDTO save(CPUDTO cpuDTO) {
        CPUEntity cpuEntity = cpuMapper.mapFrom(cpuDTO);
        CPUEntity savedCPUEntity = cpuRepository.save(cpuEntity);
        return cpuMapper.mapTo(savedCPUEntity);
    }

    @Override
    public List<CPUDTO> saveAll(List<CPUDTO> cpuDTOs) {
        List<CPUEntity> cpuEntityList = cpuDTOs.stream()
                .map(cpuMapper::mapFrom)
                .toList();

        List<CPUEntity> savedCPUEntityList = cpuRepository.saveAll(cpuEntityList);

        return savedCPUEntityList.stream()
                .map(cpuMapper::mapTo)
                .toList();
    }

    @Override
    public List<CPUDTO> findAll() {
        return cpuRepository.findAll().stream()
                .map(cpuMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<CPUDTO> findOne(String id) {
        return cpuRepository.findById(id).map(cpuMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return cpuRepository.existsById(id);
    }

    @Override
    public Optional<CPUDTO> fullUpdate(String id, CPUDTO cpuDTO) {
        return cpuRepository.findById(id).map(existing -> {
            CPUEntity cpuEntity = cpuMapper.mapFrom(cpuDTO);
            CPUEntity savedCPUEntity = cpuRepository.save(cpuEntity);
            return cpuMapper.mapTo(savedCPUEntity);
        });
    }

    @Override
    public Optional<CPUDTO> partialUpdate(String id, CPUDTO cpuDTO) {
        return cpuRepository.findById(id).map(existing -> {
            if (cpuDTO.getName() != null) existing.setName(cpuDTO.getName());
            CPUEntity savedCPU = cpuRepository.save(existing);
            return cpuMapper.mapTo(savedCPU);
        });
    }

    @Override
    public void delete(String id) {
        cpuRepository.deleteById(id);
    }
}