package com.price_tracker.services.impl;

import com.price_tracker.domain.dto.GPUWorkstationDTO;
import com.price_tracker.domain.entities.GPUWorkstationEntity;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.repositories.GPUWorkstationRepository;
import com.price_tracker.services.GPUWorkstationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GPUWorkstationServiceImpl implements GPUWorkstationService {

    private final GPUWorkstationRepository gpuWorkstationRepository;
    private final Mapper<GPUWorkstationEntity, GPUWorkstationDTO> modelMapper;

    @Override
    public GPUWorkstationDTO save(GPUWorkstationDTO gpuWorkstationDTO) {
        GPUWorkstationEntity gpuWorkstation = modelMapper.mapFrom(gpuWorkstationDTO);
        GPUWorkstationEntity savedGPUWorkstation = gpuWorkstationRepository.save(gpuWorkstation);
        return modelMapper.mapTo(savedGPUWorkstation);
    }

    @Override
    public List<GPUWorkstationDTO> saveAll(List<GPUWorkstationDTO> gpuWorkstationDTOList) {
        List<GPUWorkstationEntity> gpuWorkstationEntityList = gpuWorkstationDTOList.stream()
                .map(modelMapper::mapFrom)
                .toList();

        List<GPUWorkstationEntity> savedGPUWorktstationEntityList = gpuWorkstationRepository.
                saveAll(gpuWorkstationEntityList);

        return savedGPUWorktstationEntityList.stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public List<GPUWorkstationDTO> findAll() {
        List<GPUWorkstationEntity> gpuWorkstationEntityList = gpuWorkstationRepository.findAll();

        return gpuWorkstationEntityList.stream()
                .map(modelMapper::mapTo)
                .toList();
    }

    @Override
    public Optional<GPUWorkstationDTO> findOne(String id) {
        return gpuWorkstationRepository.findById(id).map(modelMapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return gpuWorkstationRepository.existsById(id);
    }

    @Override
    public Optional<GPUWorkstationDTO> fullUpdate(String id, GPUWorkstationDTO gpuWorkstationDTO) {

        return gpuWorkstationRepository.findById(id).map(existing -> {
            GPUWorkstationEntity gpuWorkstation = modelMapper.mapFrom(gpuWorkstationDTO);
            GPUWorkstationEntity savedGPUWorkstationEntity = gpuWorkstationRepository.save(gpuWorkstation);
            return modelMapper.mapTo(savedGPUWorkstationEntity);
        });
    }

    @Override
    public Optional<GPUWorkstationDTO> partialUpdate(String id, GPUWorkstationDTO gpuWorkstationDTO) {
        return gpuWorkstationRepository.findById(id).map(existing -> {

            // only updates the name for now
            if (gpuWorkstationDTO.getName() != null) existing.setName(gpuWorkstationDTO.getName());

            GPUWorkstationEntity savedGPUWorkstation = gpuWorkstationRepository.save(existing);
            return modelMapper.mapTo(savedGPUWorkstation);
        });
    }

    @Override
    public void delete(String id) {
        gpuWorkstationRepository.deleteById(id);
    }
}
