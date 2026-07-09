package com.price_tracker.services.product_services.impl;

import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.services.product_services.GenericProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Transactional
public class GenericProductServiceImpl<E, D> implements GenericProductService<D> {

    private final JpaRepository<E, String> repository;
    private final GenericMapper<E, D> mapper;
    private final ModelMapper nullSafeModelMapper;

    public GenericProductServiceImpl(JpaRepository<E, String> repository,
                                     GenericMapper<E, D> mapper,
                                     ModelMapper nullSafeModelMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.nullSafeModelMapper = nullSafeModelMapper;
    }

    @Override
    public D save(D dto) {
        E entity = mapper.mapFrom(dto);
        E savedEntity = repository.save(entity);
        return mapper.mapTo(savedEntity);
    }

    @Override
    public List<D> saveAll(List<D> dtos) {
        List<E> entities = dtos.stream()
                .map(mapper::mapFrom)
                .toList();

        List<E> savedEntities = repository.saveAll(entities);

        return savedEntities.stream()
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public List<D> findAll() {
        return repository.findAll().stream()
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public Optional<D> findOne(String id) {
        return repository.findById(id).map(mapper::mapTo);
    }

    @Override
    public Boolean exists(String id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<D> fullUpdate(String id, D dto) {
        return repository.findById(id).map(existing -> {
            E entity = mapper.mapFrom(dto);
            E savedEntity = repository.save(entity);
            return mapper.mapTo(savedEntity);
        });
    }

    @Override
    public Optional<D> partialUpdate(String id, D dto) {
        return repository.findById(id).map(existing -> {
            nullSafeModelMapper.map(dto, existing);
            E savedEntity = repository.save(existing);
            return mapper.mapTo(savedEntity);
        });
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}