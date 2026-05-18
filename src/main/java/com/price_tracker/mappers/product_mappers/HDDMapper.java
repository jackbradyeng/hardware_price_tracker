package com.price_tracker.mappers.product_mappers;

import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.domain.entities.product_entities.HDDEntity;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class HDDMapper implements Mapper<HDDEntity, HDDDTO> {

    private final ModelMapper modelMapper;

    @Override
    public HDDDTO mapTo(HDDEntity hddEntity) {
        return modelMapper.map(hddEntity, HDDDTO.class);
    }

    @Override
    public HDDEntity mapFrom(HDDDTO hdddto) {
        return modelMapper.map(hdddto, HDDEntity.class);
    }
}