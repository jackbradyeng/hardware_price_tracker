package com.priceTracker.repositories.productRepositories;

import com.priceTracker.domain.entities.productEntities.GPUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPURepository extends JpaRepository<GPUEntity, String> {
}