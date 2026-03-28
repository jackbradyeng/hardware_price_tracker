package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.RAMDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RAMPricePointRepository extends JpaRepository<RAMPricePoint, Long> {

    @Query("select p as RAMPricePoint, e as RAMEntity from RAMPricePoint p " +
            "left join RAMEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber")
    List<RAMDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber);
}
