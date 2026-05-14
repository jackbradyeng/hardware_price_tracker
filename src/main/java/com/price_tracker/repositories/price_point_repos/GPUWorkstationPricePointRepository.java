package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.GPUWorkstationDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.GPUWorkstationPricePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GPUWorkstationPricePointRepository extends JpaRepository<GPUWorkstationPricePoint, Long> {

    @Query(value = "select p as GPUWorkstationPricePoint, e as GPUWorkstationEntity from GPUWorkstationPricePoint p " +
            "left join GPUWorkstationEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "select count(p) from GPUWorkstationPricePoint p where p.modelNumber = :modelNumber")
    Page<GPUWorkstationDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber, Pageable pageable);
}