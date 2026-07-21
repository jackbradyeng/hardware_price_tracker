package com.priceTracker.repositories.productRepositories;

import com.priceTracker.domain.entities.productEntities.SSDEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SSDRepository extends JpaRepository<SSDEntity, String> {
}