package com.priceTracker.services.pricePointServices;

import com.priceTracker.domain.entities.scrapingJobEntities.ScrapingJobResult;
import com.priceTracker.repositories.scrapingJobRepositories.ScrapingJobResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapingJobService {

    private final ScrapingJobResultRepository scrapingJobResultRepository;

    // CREATE METHOD
    public ScrapingJobResult saveResults(String vendor, String productType,
                                         Integer recordsReceived, Integer recordsSaved) {

        ScrapingJobResult scrapingJobResult = ScrapingJobResult.builder()
                .vendor(vendor)
                .productType(productType)
                .recordsReceived(recordsReceived)
                .recordsSaved(recordsSaved)
                .timeCompleted(LocalDateTime.now())
                .build();

        return scrapingJobResultRepository.save(scrapingJobResult);
    }

    // READ METHODS
    public Optional<ScrapingJobResult> findResult(Long id) {
        return scrapingJobResultRepository.findById(id);
    }

    public List<ScrapingJobResult> findResults() {
        return scrapingJobResultRepository.findAll();
    }
}