package com.price_tracker.services;

import com.price_tracker.domain.entities.UmartProductEntity;
import java.util.List;
import java.util.Optional;

public interface UmartProductService {

    UmartProductEntity save(UmartProductEntity umartProductEntity);

    List<UmartProductEntity> findAll();

    Optional<UmartProductEntity> findOne(String id);

    boolean exists(String id);

    UmartProductEntity partialUpdate(String id, UmartProductEntity umartProductEntity);

    void delete(String id);
}
