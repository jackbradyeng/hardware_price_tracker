package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.CPUDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CPUPricePointRepository extends JpaRepository<CPUPricePoint, Long> {

    @Query(value = "select p as CPUPricePoint, e as CPUEntity from CPUPricePoint p " +
            "left join CPUEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber",
            countQuery = "select count(p) from CPUPricePoint p where p.modelNumber = :modelNumber")
    Page<CPUDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber, Pageable pageable);
}
