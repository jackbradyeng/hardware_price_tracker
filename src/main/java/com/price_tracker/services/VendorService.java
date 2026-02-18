package com.price_tracker.services;

import com.price_tracker.domain.entities.VendorEntity;
import java.util.List;
import java.util.Optional;

public interface VendorService {

    VendorEntity save(VendorEntity vendorEntity);

    List<VendorEntity> findAll();

    Optional<VendorEntity> findOne(String id);

    boolean exists(String id);

    VendorEntity partialUpdate(String id, VendorEntity vendorEntity);

    void delete(String id);
}
