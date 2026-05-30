package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.SSDDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SSDPricePointRepository extends JpaRepository<SSDPricePoint, Long> {

    @Query(value = "select p as SSDPricePoint, e as SSDEntity from SSDPricePoint p " +
            "left join SSDEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "SELECT COUNT(p) FROM SSDPricePoint p WHERE p.modelNumber = :modelNumber")
    Page<SSDDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber, Pageable pageable);
}