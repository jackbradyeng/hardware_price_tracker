package com.priceTracker.repositories.pricePointRepositories;

import com.priceTracker.domain.dto.hybridInterfaces.GenericDataAndPricePointProjection;
import com.priceTracker.domain.entities.pricePointEntities.CPUPricePoint;
import com.priceTracker.domain.entities.productEntities.CPUEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CPUPricePointRepository extends JpaRepository<CPUPricePoint, Long> {

    @Query(value = "select p as pricePoint, e as entity from CPUPricePoint p " +
            "left join CPUEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "select count(p) from CPUPricePoint p where p.modelNumber = :modelNumber")
    Page<GenericDataAndPricePointProjection<CPUEntity, CPUPricePoint>> getPricePointsByModelNumber(
            @Param("modelNumber") String modelNumber, Pageable pageable);
}