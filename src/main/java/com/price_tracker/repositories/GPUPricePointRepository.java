package com.price_tracker.repositories;

import com.price_tracker.domain.entities.GPUPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPUPricePointRepository extends JpaRepository<GPUPricePoint, Long> {
}
