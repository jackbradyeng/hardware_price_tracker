package com.price_tracker.mappers.price_point_mappers;

import com.price_tracker.domain.dto.price_point_dtos.NVMEPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NVMEPricePointMapper implements Mapper<NVMEPricePoint, NVMEPricePointDTO> {

    private final ModelMapper modelMapper;

    @Override
    public NVMEPricePointDTO mapTo(NVMEPricePoint nvmePricePoint) {
        return modelMapper.map(nvmePricePoint, NVMEPricePointDTO.class);
    }

    @Override
    public NVMEPricePoint mapFrom(NVMEPricePointDTO nvmePricePointDTO) {
        return modelMapper.map(nvmePricePointDTO, NVMEPricePoint.class);
    }
}