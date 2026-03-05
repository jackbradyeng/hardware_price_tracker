package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.CPUPricePointDTO;
import com.price_tracker.domain.entities.CPUPricePoint;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CPUPricePointMapper implements Mapper<CPUPricePoint, CPUPricePointDTO> {

    private final ModelMapper modelMapper;

    @Override
    public CPUPricePointDTO mapTo(CPUPricePoint cpuPricePoint) {
        return modelMapper.map(cpuPricePoint, CPUPricePointDTO.class);
    }

    @Override
    public CPUPricePoint mapFrom(CPUPricePointDTO cpuPricePointDTO) {
        return modelMapper.map(cpuPricePointDTO, CPUPricePoint.class);
    }
}
