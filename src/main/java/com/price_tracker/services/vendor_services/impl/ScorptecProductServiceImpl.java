package com.price_tracker.services.vendor_services.impl;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.domain.entities.vendor_entities.ScorptecProductEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.vendor_repos.ScorptecProductRepository;
import com.price_tracker.services.vendor_services.GenericVendorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ScorptecProductServiceImpl implements GenericVendorService<VendorProductDTO> {

    private final ScorptecProductRepository scorptecProductRepository;
    private final ModelMapper nullSafeModelMapper;
    private final GenericMapper<ScorptecProductEntity, VendorProductDTO> mapper;

    @Autowired
    public ScorptecProductServiceImpl(ScorptecProductRepository scorptecProductRepository,
                                      ModelMapper nullSafeModelMapper,
                                      MapperFactory mapperFactory) {
        this.scorptecProductRepository = scorptecProductRepository;
        this.nullSafeModelMapper = nullSafeModelMapper;
        this.mapper = mapperFactory.create(ScorptecProductEntity.class, VendorProductDTO.class);
    }

    @Override
    public VendorProductDTO save(VendorProductDTO scorptecProductDTO) {

        ScorptecProductEntity scorptecProductEntity = nullSafeModelMapper
                .map(scorptecProductDTO, ScorptecProductEntity.class);

        ScorptecProductEntity savedScorptecProduct = scorptecProductRepository.save(scorptecProductEntity);

        return nullSafeModelMapper.map(savedScorptecProduct, VendorProductDTO.class);
    }

    @Override
    public List<VendorProductDTO> saveAll(List<VendorProductDTO> dtos) {
        List<ScorptecProductEntity> entities = dtos.stream()
                .map(mapper::mapFrom)
                .toList();

        List<ScorptecProductEntity> savedEntities = scorptecProductRepository.saveAll(entities);

        return savedEntities.stream()
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public Optional<VendorProductDTO> findOne(String id) {
        return scorptecProductRepository.findById(id).map(mapper::mapTo);
    }

    @Override
    public List<VendorProductDTO> findAll() {
        return scorptecProductRepository
                .findAll()
                .stream()
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public Boolean exists(String id) {
        return scorptecProductRepository.existsById(id);
    }

    @Override
    public Optional<VendorProductDTO> partialUpdate(String id, VendorProductDTO dto) {

        Optional<ScorptecProductEntity> existingOptional = scorptecProductRepository.findById(id);

        // returns empty if the specified ID is not found
        if (existingOptional.isEmpty()) {
            return Optional.empty();
        }
        ScorptecProductEntity existing = existingOptional.get();

        // match the IDs
        dto.setId(Long.parseLong(id));

        // map the partial updates from the provided DTO to the new object
        nullSafeModelMapper.map(dto, existing);

        ScorptecProductEntity updatedEntity = scorptecProductRepository.save(existing);
        return Optional.of(mapper.mapTo(updatedEntity));
    }

    @Override
    public Optional<VendorProductDTO> fullUpdate(String id, VendorProductDTO dto) {

        // returns empty if the specified ID is not found
        if (!this.exists(id)) {
            return Optional.empty();
        }

        // matches the ID in the provided VendorProductDTO to avoid updating the wrong resource
        dto.setId(Long.parseLong(id));

        Optional<ScorptecProductEntity> updatedScorptecProduct = Optional.ofNullable(nullSafeModelMapper
                .map(dto, ScorptecProductEntity.class));

        return updatedScorptecProduct
                .map(scorptecProductRepository::save)
                .map(mapper::mapTo);
    }

    @Override
    public void delete(String id) {
        scorptecProductRepository.deleteById(id);
    }
}