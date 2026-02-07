package com.price_tracker.repositories;

import com.price_tracker.domain.RAM;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RAMRepository extends CrudRepository<RAM, String> {
}
