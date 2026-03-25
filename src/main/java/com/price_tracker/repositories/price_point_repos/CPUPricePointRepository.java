package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.CPUDataANdPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CPUPricePointRepository extends JpaRepository<CPUPricePoint, Long> {

    @Query("select p as CPUPricePoint, e as CPUEntity from CPUPricePoint p " +
            "left join CPUEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber")
    List<CPUDataANdPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber);
}
