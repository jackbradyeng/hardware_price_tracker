package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.UmartProductDTO;
import com.price_tracker.domain.entities.UmartProductEntity;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UmartProductMapper implements Mapper<UmartProductEntity, UmartProductDTO> {

    private final ModelMapper modelMapper;

    @Override
    public UmartProductDTO mapTo(UmartProductEntity umartProductEntity) {
        return modelMapper.map(umartProductEntity, UmartProductDTO.class);
    }

    @Override
    public UmartProductEntity mapFrom(UmartProductDTO umartProductDTO) {
        return modelMapper.map(umartProductDTO, UmartProductEntity.class);
    }
}
