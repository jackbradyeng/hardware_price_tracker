package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.domain.entities.GPU;
import com.price_tracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GPUMapper implements Mapper<GPU, GPUDTO> {

    private ModelMapper modelMapper;

    public GPUMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public GPUDTO mapTo(GPU gpu) {
        return modelMapper.map(gpu, GPUDTO.class);
    }

    @Override
    public GPU mapFrom(GPUDTO gpudto) {
        return modelMapper.map(gpudto, GPU.class);
    }
}
