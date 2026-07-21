package com.priceTracker.services.productServices;

import java.util.List;
import java.util.Optional;

public interface GenericProductService<D> {

    D save(D dto);

    List<D> saveAll(List<D> dtos);

    List<D> findAll();

    Optional<D> findOne(String id);

    Boolean exists(String id);

    Optional<D> fullUpdate(String id, D dto);

    Optional<D> partialUpdate(String id, D dto);

    void delete(String id);
}