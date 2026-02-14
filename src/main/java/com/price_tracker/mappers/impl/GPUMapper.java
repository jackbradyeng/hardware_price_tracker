package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GPUMapper implements Mapper<GPUEntity, GPUDTO> {

    private final ModelMapper modelMapper;

    public GPUMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public GPUDTO mapTo(GPUEntity gpuEntity) {
        return modelMapper.map(gpuEntity, GPUDTO.class);
    }

    @Override
    public GPUEntity mapFrom(GPUDTO gpudto) {
        return modelMapper.map(gpudto, GPUEntity.class);
    }
}
