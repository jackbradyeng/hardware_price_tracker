package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.GPUWorkstationDTO;
import com.price_tracker.domain.entities.GPUWorkstationEntity;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GPUWorkstationMapper implements Mapper<GPUWorkstationEntity, GPUWorkstationDTO> {

    private final ModelMapper modelMapper;

    @Override
    public GPUWorkstationDTO mapTo(GPUWorkstationEntity gpuWorkstationEntity) {
        return modelMapper.map(gpuWorkstationEntity, GPUWorkstationDTO.class);
    }

    @Override
    public GPUWorkstationEntity mapFrom(GPUWorkstationDTO gpuWorkstationDTO) {
        return modelMapper.map(gpuWorkstationDTO, GPUWorkstationEntity.class);
    }
}
