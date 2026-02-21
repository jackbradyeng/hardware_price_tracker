package com.price_tracker.repositories;

import com.price_tracker.domain.entities.UmartProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UmartProductRepository extends JpaRepository<UmartProductEntity, String> {
}
