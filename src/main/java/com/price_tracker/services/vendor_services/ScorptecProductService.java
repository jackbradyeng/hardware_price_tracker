package com.price_tracker.services.vendor_services;

import com.price_tracker.domain.entities.vendor_entities.ScorptecProductEntity;
import java.util.List;
import java.util.Optional;

public interface ScorptecProductService {

    ScorptecProductEntity save(ScorptecProductEntity scorptecProductEntity);

    List<ScorptecProductEntity> findAll();

    Optional<ScorptecProductEntity> findOne(String id);

    boolean exists(String id);

    ScorptecProductEntity partialUpdate(String id, ScorptecProductEntity scorptecProductEntity);

    void delete(String id);
}