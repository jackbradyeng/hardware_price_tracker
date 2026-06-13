package com.price_tracker.services.product_services.impl;

import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.GPUWorkstationRepository;
import com.price_tracker.services.product_services.GPUWorkstationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GPUWorkstationServiceImpl implements GPUWorkstationService {

    private final GPUWorkstationRepository gpuWorkstationRepository;
    private final GenericMapper<GPUWorkstationEntity, GPUWorkstationDTO> gpuWorkstationMapper;

    @Autowired
    public GPUWorkstationServiceImpl(GPUWorkstationRepository gpuWorkstationRepository, MapperFactory mapperFactory) {
        this.gpuWorkstationRepository = gpuWorkstationRepository;
        this.gpuWorkstationMapper = mapperFactory.create(GPUWorkstationEntity.class, GPUWorkstationDTO.class);
    }

    @Override
    public GPUWorkstationDTO save(GPUWorkstationDTO gpuWorkstationDTO) {
        GPUWorkstationEntity gpuWorkstation = gpuWorkstationMapper.mapFrom(gpuWorkstationDTO);
        GPUWorkstationEntity savedGPUWorkstation = gpuWorkstationRepository.save(gpuWorkstation);
        return gpuWorkstationMapper.mapTo(savedGPUWorkstation);
    }

    @Override
    public List<GPUWorkstationDTO> saveAll(List<GPUWorkstationDTO> gpuWorkstationDTOList) {
        List<GPUWorkstationEntity> gpuWorkstationEntityList = gpuWorkstationDTOList.stream()
                .map(gpuWorkstationMapper::mapFrom)
                .toList();

        List<GPUWorkstationEntity> savedGPUWorktstationEntityList = gpuWorkstationRepository.
                saveAll(gpuWorkstationEntityList);

        return savedGPUWorktstationEntityList.stream()
                .map(gpuWorkstationMapper::mapTo)
                .toList();
    }

    @Override
    public List<GPUWorkstationDTO> findAll() {
        List<GPUWorkstationEntity> gpuWorkstationEntityList = gpuWorkstationRepository.findAll();

        return gpuWorkstationEntityList.stream()
                .map(gpuWorkstationMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<GPUWorkstationDTO> findOne(String id) {
        return gpuWorkstationRepository.findById(id).map(gpuWorkstationMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return gpuWorkstationRepository.existsById(id);
    }

    @Override
    public Optional<GPUWorkstationDTO> fullUpdate(String id, GPUWorkstationDTO gpuWorkstationDTO) {

        return gpuWorkstationRepository.findById(id).map(existing -> {
            GPUWorkstationEntity gpuWorkstation = gpuWorkstationMapper.mapFrom(gpuWorkstationDTO);
            GPUWorkstationEntity savedGPUWorkstationEntity = gpuWorkstationRepository.save(gpuWorkstation);
            return gpuWorkstationMapper.mapTo(savedGPUWorkstationEntity);
        });
    }

    @Override
    public Optional<GPUWorkstationDTO> partialUpdate(String id, GPUWorkstationDTO gpuWorkstationDTO) {
        return gpuWorkstationRepository.findById(id).map(existing -> {

            // only updates the name for now
            if (gpuWorkstationDTO.getName() != null) existing.setName(gpuWorkstationDTO.getName());

            GPUWorkstationEntity savedGPUWorkstation = gpuWorkstationRepository.save(existing);
            return gpuWorkstationMapper.mapTo(savedGPUWorkstation);
        });
    }

    @Override
    public void delete(String id) {
        gpuWorkstationRepository.deleteById(id);
    }
}
