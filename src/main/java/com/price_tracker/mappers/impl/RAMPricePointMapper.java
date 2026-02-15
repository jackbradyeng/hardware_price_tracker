package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.RAMPricePointDTO;
import com.price_tracker.domain.entities.RAMPricePoint;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RAMPricePointMapper implements Mapper<RAMPricePoint, RAMPricePointDTO> {

    private final ModelMapper modelMapper;

    @Override
    public RAMPricePointDTO mapTo(RAMPricePoint ramPricePoint) {
        return modelMapper.map(ramPricePoint, RAMPricePointDTO.class);
    }

    @Override
    public RAMPricePoint mapFrom(RAMPricePointDTO ramPricePointDTO) {
        return modelMapper.map(ramPricePointDTO, RAMPricePoint.class);
    }
}
