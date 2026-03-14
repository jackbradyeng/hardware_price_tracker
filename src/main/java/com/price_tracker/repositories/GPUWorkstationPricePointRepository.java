package com.price_tracker.repositories;

import com.price_tracker.domain.entities.GPUWorkstationPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPUWorkstationPricePointRepository extends JpaRepository<GPUWorkstationPricePoint, Long> {
}
