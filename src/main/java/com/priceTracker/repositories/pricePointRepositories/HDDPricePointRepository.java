package com.priceTracker.repositories.pricePointRepositories;

import com.priceTracker.domain.dto.hybridInterfaces.GenericDataAndPricePointProjection;
import com.priceTracker.domain.entities.pricePointEntities.HDDPricePoint;
import com.priceTracker.domain.entities.productEntities.HDDEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HDDPricePointRepository extends JpaRepository<HDDPricePoint, Long> {

    @Query(value = "select p as pricePoint, e as entity from HDDPricePoint p " +
            "left join HDDEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "SELECT COUNT(p) FROM HDDPricePoint p WHERE p.modelNumber = :modelNumber")
    Page<GenericDataAndPricePointProjection<HDDEntity, HDDPricePoint>> getPricePointsByModelNumber(
            @Param("modelNumber") String modelNumber, Pageable pageable);
}