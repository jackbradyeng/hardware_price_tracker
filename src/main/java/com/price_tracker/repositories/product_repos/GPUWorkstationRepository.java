package com.price_tracker.repositories.product_repos;

import com.price_tracker.domain.entities.GPUWorkstationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPUWorkstationRepository extends JpaRepository<GPUWorkstationEntity, String> {
}
