package com.priceTracker.repositories.pricePointRepositories;

import com.priceTracker.domain.dto.hybridInterfaces.GenericDataAndPricePointProjection;
import com.priceTracker.domain.entities.pricePointEntities.GPUWorkstationPricePoint;
import com.priceTracker.domain.entities.productEntities.GPUWorkstationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GPUWorkstationPricePointRepository extends JpaRepository<GPUWorkstationPricePoint, Long> {

    @Query(value = "select p as pricePoint, e as entity from GPUWorkstationPricePoint p " +
            "left join GPUWorkstationEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "select count(p) from GPUWorkstationPricePoint p where p.modelNumber = :modelNumber")
    Page<GenericDataAndPricePointProjection<GPUWorkstationEntity, GPUWorkstationPricePoint>> getPricePointsByModelNumber(
            @Param("modelNumber") String modelNumber, Pageable pageable);
}