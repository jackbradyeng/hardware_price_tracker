package com.price_tracker.repositories;

import com.price_tracker.domain.entities.RAMPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RAMPricePointRepository extends JpaRepository<RAMPricePoint, Long> {
}
