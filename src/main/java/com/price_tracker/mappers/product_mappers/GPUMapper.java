package com.price_tracker.mappers.product_mappers;

import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GPUMapper implements Mapper<GPUEntity, GPUDTO> {

    private final ModelMapper modelMapper;

    @Override
    public GPUDTO mapTo(GPUEntity gpuEntity) {
        return modelMapper.map(gpuEntity, GPUDTO.class);
    }

    @Override
    public GPUEntity mapFrom(GPUDTO gpudto) {
        return modelMapper.map(gpudto, GPUEntity.class);
    }
}
