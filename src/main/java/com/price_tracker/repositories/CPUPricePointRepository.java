package com.price_tracker.repositories;

import com.price_tracker.domain.entities.CPUPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CPUPricePointRepository extends JpaRepository<CPUPricePoint, Long> {
}
