package com.priceTracker.repositories.scrapingJobRepositories;

import com.priceTracker.domain.entities.scrapingJobEntities.ScrapingJobResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapingJobResultRepository extends JpaRepository<ScrapingJobResult, Long> {
}