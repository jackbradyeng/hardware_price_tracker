package com.price_tracker.services.vendor_services;

import java.util.List;
import java.util.Optional;

public interface GenericVendorService<D> {

    D save(D dto);

    List<D> saveAll(List<D> dtos);

    List<D> findAll();

    Optional<D> findOne(String id);

    Boolean exists(String id);

    Optional<D> partialUpdate(String id, D dto);

    Optional<D> fullUpdate(String id, D dto);

    void delete(String id);
}