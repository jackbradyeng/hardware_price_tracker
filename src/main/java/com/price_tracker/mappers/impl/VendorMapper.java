package com.price_tracker.mappers.impl;

import com.price_tracker.domain.dto.VendorDTO;
import com.price_tracker.domain.entities.VendorEntity;
import com.price_tracker.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class VendorMapper implements Mapper<VendorEntity, VendorDTO> {

    private final ModelMapper modelMapper;

    @Override
    public VendorDTO mapTo(VendorEntity vendorEntity) {
        return modelMapper.map(vendorEntity, VendorDTO.class);
    }

    @Override
    public VendorEntity mapFrom(VendorDTO vendorDTO) {
        return modelMapper.map(vendorDTO, VendorEntity.class);
    }
}
