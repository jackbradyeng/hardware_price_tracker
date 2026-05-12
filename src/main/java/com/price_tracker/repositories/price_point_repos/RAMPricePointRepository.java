package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.RAMDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RAMPricePointRepository extends JpaRepository<RAMPricePoint, Long> {

    @Query(value = "select p as RAMPricePoint, e as RAMEntity from RAMPricePoint p " +
            "left join RAMEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "select count(p) from RAMPricePoint p where p.modelNumber = :modelNumber")
    Page<RAMDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber, Pageable pageable);
}
