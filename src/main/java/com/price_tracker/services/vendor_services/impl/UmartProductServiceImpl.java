package com.price_tracker.services.vendor_services.impl;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.services.vendor_services.GenericVendorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UmartProductServiceImpl implements GenericVendorService<VendorProductDTO> {

    private final UmartProductRepository umartProductRepository;
    private final ModelMapper nullSafeModelMapper;
    private final GenericMapper<UmartProductEntity, VendorProductDTO> mapper;

    @Autowired
    public UmartProductServiceImpl(UmartProductRepository umartProductRepository,
                                   ModelMapper nullSafeModelMapper,
                                   MapperFactory mapperFactory) {
        this.umartProductRepository = umartProductRepository;
        this.nullSafeModelMapper = nullSafeModelMapper;
        this.mapper = mapperFactory.create(UmartProductEntity.class, VendorProductDTO.class);
    }

    @Override
    public VendorProductDTO save(VendorProductDTO umartProductDTO) {

        UmartProductEntity umartProductEntity = nullSafeModelMapper
                .map(umartProductDTO, UmartProductEntity.class);

        UmartProductEntity savedUmartProduct = umartProductRepository.save(umartProductEntity);

        return nullSafeModelMapper.map(savedUmartProduct, VendorProductDTO.class);
    }

    @Override
    public List<VendorProductDTO> saveAll(List<VendorProductDTO> dtos) {
        List<UmartProductEntity> entities = dtos.stream()
                .map(mapper::mapFrom)
                .toList();

        List<UmartProductEntity> savedEntities = umartProductRepository.saveAll(entities);

        return savedEntities.stream()
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public Optional<VendorProductDTO> findOne(String id) {
        return umartProductRepository.findById(id).map(mapper::mapTo);
    }

    @Override
    public List<VendorProductDTO> findAll() {
        return umartProductRepository
                .findAll()
                .stream()
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public Boolean exists(String id) {
        return umartProductRepository.existsById(id);
    }

    @Override
    public Optional<VendorProductDTO> partialUpdate(String id, VendorProductDTO dto) {

        Optional<UmartProductEntity> existingOptional = umartProductRepository.findById(id);

        if (existingOptional.isEmpty()) {
            return Optional.empty();
        }

        UmartProductEntity existing = existingOptional.get();
        dto.setId(Long.parseLong(id));
        nullSafeModelMapper.map(dto, existing);
        UmartProductEntity updatedEntity = umartProductRepository.save(existing);
        return Optional.of(mapper.mapTo(updatedEntity));
    }

    @Override
    public Optional<VendorProductDTO> fullUpdate(String id, VendorProductDTO dto) {

        if (!this.exists(id)) {
            return Optional.empty();
        }

        dto.setId(Long.parseLong(id));

        Optional<UmartProductEntity> updatedUmartProduct = Optional.ofNullable(nullSafeModelMapper
                .map(dto, UmartProductEntity.class));

        return updatedUmartProduct
                .map(umartProductRepository::save)
                .map(mapper::mapTo);
    }

    @Override
    public void delete(String id) {
        umartProductRepository.deleteById(id);
    }
}