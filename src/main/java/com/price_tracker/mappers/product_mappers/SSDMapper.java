package com.price_tracker.mappers.product_mappers;

import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SSDMapper implements Mapper<SSDEntity, SSDDTO> {

    private final ModelMapper modelMapper;

    @Override
    public SSDDTO mapTo(SSDEntity ssdEntity) {
        return modelMapper.map(ssdEntity, SSDDTO.class);
    }

    @Override
    public SSDEntity mapFrom(SSDDTO ssddto) {
        return modelMapper.map(ssddto, SSDEntity.class);
    }
}