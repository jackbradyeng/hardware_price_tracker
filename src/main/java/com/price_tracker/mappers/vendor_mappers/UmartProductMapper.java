package com.price_tracker.mappers.vendor_mappers;

import com.price_tracker.domain.dto.vendor_dtos.UmartProductDTO;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
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
