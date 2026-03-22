package com.price_tracker.repositories.price_point_repos;

import com.price_tracker.domain.dto.hybrid_interfaces.GPUDataAndPricePointProjection;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GPUPricePointRepository extends JpaRepository<GPUPricePoint, Long> {

    /** currently this method will return ALL price points present in the DB. As the volume of price points scales, it
     * will need to be refactored so that it returns a tolerable amount (i.e. all from the previous year). Otherwise,
     * there may be problems with RAM usage and networking latency. I've decided to just go ahead and BUILD FIRST
     * however we will OPTIMIZE LATER. */
    @Query("select p as GPUPricePoint, e as GPUEntity from GPUPricePoint p " + // Aliases updated
            "left join GPUEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber")
    List<GPUDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber);
}
