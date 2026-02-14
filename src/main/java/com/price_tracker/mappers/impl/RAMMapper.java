package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.RAMDTO;
import com.price_tracker.domain.entities.RAMEntity;
import com.price_tracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RAMMapper implements Mapper<RAMEntity, RAMDTO> {

    private final ModelMapper modelMapper;

    public RAMMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RAMDTO mapTo(RAMEntity ramEntity) {
        return modelMapper.map(ramEntity, RAMDTO.class);
    }

    @Override
    public RAMEntity mapFrom(RAMDTO ramdto) {
        return modelMapper.map(ramdto, RAMEntity.class);
    }
}
