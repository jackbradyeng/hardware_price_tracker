package com.price_tracker.mappers.product_mappers;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CPUMapper implements Mapper<CPUEntity, CPUDTO> {

    private final ModelMapper modelMapper;

    @Override
    public CPUDTO mapTo(CPUEntity cpuEntity) {
        return modelMapper.map(cpuEntity, CPUDTO.class);
    }

    @Override
    public CPUEntity mapFrom(CPUDTO cpudto) {
        return modelMapper.map(cpudto, CPUEntity.class);
    }
}
