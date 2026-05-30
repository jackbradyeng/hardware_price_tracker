package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.HDDDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.HDDPricePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HDDPricePointRepository extends JpaRepository<HDDPricePoint, Long> {

    @Query(value = "select p as HDDPricePoint, e as HDDEntity from HDDPricePoint p " +
            "left join HDDEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "SELECT COUNT(p) FROM HDDPricePoint p WHERE p.modelNumber = :modelNumber")
    Page<HDDDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber, Pageable pageable);
}