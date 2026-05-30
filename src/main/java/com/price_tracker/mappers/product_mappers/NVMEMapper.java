package com.price_tracker.mappers.product_mappers;

import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class NVMEMapper implements Mapper<NVMEEntity, NVMEDTO> {

    private final ModelMapper modelMapper;

    @Override
    public NVMEDTO mapTo(NVMEEntity nvmeEntity) {
        return modelMapper.map(nvmeEntity, NVMEDTO.class);
    }

    @Override
    public NVMEEntity mapFrom(NVMEDTO nvmedto) {
        return modelMapper.map(nvmedto, NVMEEntity.class);
    }
}