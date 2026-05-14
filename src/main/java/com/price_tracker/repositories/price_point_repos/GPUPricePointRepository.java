package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.GPUDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GPUPricePointRepository extends JpaRepository<GPUPricePoint, Long> {

    @Query(value = "select p as GPUPricePoint, e as GPUEntity from GPUPricePoint p " +
            "left join GPUEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "select count(p) from GPUPricePoint p where p.modelNumber = :modelNumber")
    Page<GPUDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber, Pageable pageable);
}
