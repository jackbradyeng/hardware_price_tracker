package com.priceTracker.repositories.pricePointRepositories;

import com.priceTracker.domain.dto.hybridInterfaces.GenericDataAndPricePointProjection;
import com.priceTracker.domain.entities.pricePointEntities.SSDPricePoint;
import com.priceTracker.domain.entities.productEntities.SSDEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SSDPricePointRepository extends JpaRepository<SSDPricePoint, Long> {

    @Query(value = "select p as pricePoint, e as entity from SSDPricePoint p " +
            "left join SSDEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "SELECT COUNT(p) FROM SSDPricePoint p WHERE p.modelNumber = :modelNumber")
    Page<GenericDataAndPricePointProjection<SSDEntity, SSDPricePoint>> getPricePointsByModelNumber(
            @Param("modelNumber") String modelNumber, Pageable pageable);
}