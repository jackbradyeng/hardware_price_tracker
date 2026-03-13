package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.GPUWorkstationPricePointDTO;
import com.price_tracker.domain.entities.GPUWorkstationPricePoint;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GPUWorkstationPricePointMapper implements Mapper<GPUWorkstationPricePoint, GPUWorkstationPricePointDTO> {

    private final ModelMapper modelMapper;

    @Override
    public GPUWorkstationPricePointDTO mapTo(GPUWorkstationPricePoint gpuWorkstationPricePoint) {
        return modelMapper.map(gpuWorkstationPricePoint, GPUWorkstationPricePointDTO.class);
    }

    @Override
    public GPUWorkstationPricePoint mapFrom(GPUWorkstationPricePointDTO gpuWorkstationPricePointDTO) {
        return modelMapper.map(gpuWorkstationPricePointDTO, GPUWorkstationPricePoint.class);
    }
}
