package com.priceTracker.repositories.productRepositories;

import com.priceTracker.domain.entities.productEntities.GPUWorkstationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPUWorkstationRepository extends JpaRepository<GPUWorkstationEntity, String> {
}