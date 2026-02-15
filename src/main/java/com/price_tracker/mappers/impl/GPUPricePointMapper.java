package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.GPUPricePointDTO;
import com.price_tracker.domain.entities.GPUPricePoint;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GPUPricePointMapper implements Mapper<GPUPricePoint, GPUPricePointDTO> {

    private final ModelMapper modelMapper;

    @Override
    public GPUPricePointDTO mapTo(GPUPricePoint gpuPricePoint) {
        return modelMapper.map(gpuPricePoint, GPUPricePointDTO.class);
    }

    @Override
    public GPUPricePoint mapFrom(GPUPricePointDTO gpuPricePointDTO) {
        return modelMapper.map(gpuPricePointDTO, GPUPricePoint.class);
    }
}
