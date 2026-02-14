package com.price_tracker.repositories;

import com.price_tracker.domain.entities.RAMEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RAMRepository extends CrudRepository<RAMEntity, String> {
}
