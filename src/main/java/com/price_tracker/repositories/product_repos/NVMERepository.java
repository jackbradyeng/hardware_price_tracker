package com.price_tracker.repositories.product_repos;

import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NVMERepository extends JpaRepository<NVMEEntity, String> {
}