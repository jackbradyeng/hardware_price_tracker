package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.GenericDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GPUPricePointRepository extends JpaRepository<GPUPricePoint, Long> {

    @Query(value = "select p as pricePoint, e as entity from GPUPricePoint p " +
            "left join GPUEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "select count(p) from GPUPricePoint p where p.modelNumber = :modelNumber")
    Page<GenericDataAndPricePointProjection<GPUEntity, GPUPricePoint>> getPricePointsByModelNumber(
            @Param("modelNumber") String modelNumber, Pageable pageable);
}