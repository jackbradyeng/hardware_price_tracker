package com.priceTracker.repositories.productRepositories;

import com.priceTracker.domain.entities.productEntities.CPUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CPURepository extends JpaRepository<CPUEntity, String> {
}