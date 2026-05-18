package com.price_tracker.mappers.price_point_mappers;

import com.price_tracker.domain.dto.price_point_dtos.SSDPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SSDPricePointMapper implements Mapper<SSDPricePoint, SSDPricePointDTO> {

    private final ModelMapper modelMapper;

    @Override
    public SSDPricePointDTO mapTo(SSDPricePoint ssdPricePoint) {
        return modelMapper.map(ssdPricePoint, SSDPricePointDTO.class);
    }

    @Override
    public SSDPricePoint mapFrom(SSDPricePointDTO ssdPricePointDTO) {
        return modelMapper.map(ssdPricePointDTO, SSDPricePoint.class);
    }
}