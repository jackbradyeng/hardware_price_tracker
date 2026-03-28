package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.GPUWorkstationDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.GPUWorkstationPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GPUWorkstationPricePointRepository extends JpaRepository<GPUWorkstationPricePoint, Long> {

    @Query("select p as GPUWorkstationPricePoint, e as GPUWorkstationEntity from GPUWorkstationPricePoint p " +
            "left join GPUWorkstationEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber")
    List<GPUWorkstationDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber);
}