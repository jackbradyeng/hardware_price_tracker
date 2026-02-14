package com.price_tracker.repositories;

import com.price_tracker.domain.entities.GPUEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPURepository extends CrudRepository<GPUEntity, String> {
}
