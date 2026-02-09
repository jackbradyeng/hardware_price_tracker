package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.RAMDTO;
import com.price_tracker.domain.entities.RAM;
import com.price_tracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RAMMapper implements Mapper<RAM, RAMDTO> {

    private ModelMapper modelMapper;

    public RAMMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RAMDTO mapTo(RAM ram) {
        return modelMapper.map(ram, RAMDTO.class);
    }

    @Override
    public RAM mapFrom(RAMDTO ramdto) {
        return modelMapper.map(ramdto, RAM.class);
    }
}
