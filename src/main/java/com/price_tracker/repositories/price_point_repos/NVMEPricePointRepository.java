package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.NVMEDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NVMEPricePointRepository extends JpaRepository<NVMEPricePoint, Long> {

    @Query(value = "select p as NVMEPricePoint, e as NVMEEntity from NVMEPricePoint p " +
            "left join NVMEEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "SELECT COUNT(p) FROM NVMEPricePoint p WHERE p.modelNumber = :modelNumber")
    Page<NVMEDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber, Pageable pageable);
}