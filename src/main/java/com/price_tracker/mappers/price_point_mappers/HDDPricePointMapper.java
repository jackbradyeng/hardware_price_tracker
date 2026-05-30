package com.price_tracker.mappers.price_point_mappers;

import com.price_tracker.domain.dto.price_point_dtos.HDDPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.HDDPricePoint;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HDDPricePointMapper implements Mapper<HDDPricePoint, HDDPricePointDTO> {

    private final ModelMapper modelMapper;

    @Override
    public HDDPricePointDTO mapTo(HDDPricePoint hddPricePoint) {
        return modelMapper.map(hddPricePoint, HDDPricePointDTO.class);
    }

    @Override
    public HDDPricePoint mapFrom(HDDPricePointDTO hddPricePointDTO) {
        return modelMapper.map(hddPricePointDTO, HDDPricePoint.class);
    }
}