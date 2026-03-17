package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CPUPricePointRepository extends JpaRepository<CPUPricePoint, Long> {
}
