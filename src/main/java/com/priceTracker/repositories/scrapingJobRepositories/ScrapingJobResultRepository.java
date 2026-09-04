package com.priceTracker.repositories.scrapingJobRepositories;

import com.priceTracker.domain.entities.scrapingJobEntities.ScrapingJobResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScrapingJobResultRepository extends JpaRepository<ScrapingJobResult, Long> {

    @Query(value = "select s from ScrapingJobResult s " +
            "where s.productType = :productType")
    List<ScrapingJobResult> findScrapingJobsByProduct(@Param("productType") String productType);

    @Query(value = "select s from ScrapingJobResult s " +
            "where s.vendor = :vendor")
    List<ScrapingJobResult> findScrapingJobsByVendor(@Param("vendor") String vendor);
}