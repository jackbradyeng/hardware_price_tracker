package com.price_tracker.repositories.product_repos;

import com.price_tracker.domain.entities.product_entities.SSDEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SSDRepository extends JpaRepository<SSDEntity, String> {
}