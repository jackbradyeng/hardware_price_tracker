package com.price_tracker.services.vendor_services.impl;

import com.price_tracker.domain.dto.vendor_dtos.VendorDTO;
import com.price_tracker.domain.entities.vendor_entities.VendorEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.vendor_repos.VendorRepository;
import com.price_tracker.services.vendor_services.GenericVendorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceImpl implements GenericVendorService<VendorDTO> {

    private final VendorRepository vendorRepository;
    private final ModelMapper nullSafeModelMapper;
    private final GenericMapper<VendorEntity, VendorDTO> mapper;

    @Autowired
    public VendorServiceImpl(VendorRepository vendorRepository,
                             ModelMapper nullSafeModelMapper,
                             MapperFactory mapperFactory) {
        this.vendorRepository = vendorRepository;
        this.nullSafeModelMapper = nullSafeModelMapper;
        this.mapper = mapperFactory.create(VendorEntity.class, VendorDTO.class);
    }

    @Override
    public VendorDTO save(VendorDTO vendorDTO) {

        VendorEntity vendorEntity = nullSafeModelMapper
                .map(vendorDTO, VendorEntity.class);

        VendorEntity savedVendor = vendorRepository.save(vendorEntity);

        return nullSafeModelMapper.map(savedVendor, VendorDTO.class);
    }

    @Override
    public List<VendorDTO> saveAll(List<VendorDTO> dtos) {
        List<VendorEntity> entities = dtos.stream()
                .map(mapper::mapFrom)
                .toList();

        List<VendorEntity> savedEntities = vendorRepository.saveAll(entities);

        return savedEntities.stream()
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public Optional<VendorDTO> findOne(String id) {
        return vendorRepository.findById(id).map(mapper::mapTo);
    }

    @Override
    public List<VendorDTO> findAll() {
        return vendorRepository
                .findAll()
                .stream()
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public Boolean exists(String id) {
        return vendorRepository.existsById(id);
    }

    @Override
    public Optional<VendorDTO> partialUpdate(String id, VendorDTO dto) {

        Optional<VendorEntity> existingOptional = vendorRepository.findById(id);

        if (existingOptional.isEmpty()) {
            return Optional.empty();
        }

        VendorEntity existing = existingOptional.get();
        dto.setVendor(id);
        nullSafeModelMapper.map(dto, existing);
        VendorEntity updatedEntity = vendorRepository.save(existing);
        return Optional.of(mapper.mapTo(updatedEntity));
    }

    @Override
    public Optional<VendorDTO> fullUpdate(String id, VendorDTO dto) {

        if (!this.exists(id)) {
            return Optional.empty();
        }

        dto.setVendor(id);

        Optional<VendorEntity> updatedVendor = Optional.ofNullable(nullSafeModelMapper
                .map(dto, VendorEntity.class));

        return updatedVendor
                .map(vendorRepository::save)
                .map(mapper::mapTo);
    }

    @Override
    public void delete(String id) {
        vendorRepository.deleteById(id);
    }
}