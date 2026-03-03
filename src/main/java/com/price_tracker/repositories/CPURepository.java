package com.price_tracker.repositories;

import com.price_tracker.domain.entities.CPUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CPURepository extends JpaRepository<CPUEntity, String> {
}
