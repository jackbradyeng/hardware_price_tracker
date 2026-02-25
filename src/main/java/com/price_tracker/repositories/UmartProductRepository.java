package com.price_tracker.repositories;

import com.price_tracker.domain.entities.UmartProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UmartProductRepository extends JpaRepository<UmartProductEntity, String> {

    // JPA mapping query used to extract GPU product URLs from the DB
    @Query("select u.url from UmartProductEntity u " +
            "join GPUEntity g on u.modelNumber = g.modelNumber " +
            "where u.productType='GPU' and g.isActive = true")
    List<String> findUrlsForActiveGPUs();

    @Query("select u.url from UmartProductEntity u " +
            "join RAMEntity r on u.modelNumber = r.modelNumber " +
            "where u.productType='RAM' and r.isActive = true")
    List<String> findUrlsForActiveRAM();
}
