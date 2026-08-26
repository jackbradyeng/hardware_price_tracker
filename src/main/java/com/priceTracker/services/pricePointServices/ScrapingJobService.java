package com.priceTracker.services.pricePointServices;

import com.priceTracker.domain.dto.scrapingJobDTOs.ScrapingJobResultDTO;
import com.priceTracker.domain.entities.scrapingJobEntities.ScrapingJobResult;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.scrapingJobRepositories.ScrapingJobResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScrapingJobService {

    private final ScrapingJobResultRepository scrapingJobResultRepository;
    private final GenericMapper<ScrapingJobResult, ScrapingJobResultDTO> mapper;

    @Autowired
    public ScrapingJobService(ScrapingJobResultRepository scrapingJobResultRepository,
                              MapperFactory mapperFactory) {
        this.scrapingJobResultRepository = scrapingJobResultRepository;
        this.mapper = mapperFactory.create(ScrapingJobResult.class, ScrapingJobResultDTO.class);
    }

    // CREATE METHOD
    public ScrapingJobResultDTO saveResults(String vendor, String productType,
                                            Integer recordsReceived, Integer recordsSaved) {

        ScrapingJobResult scrapingJobResult = ScrapingJobResult.builder()
                .vendor(vendor)
                .productType(productType)
                .recordsReceived(recordsReceived)
                .recordsSaved(recordsSaved)
                .timeCompleted(LocalDateTime.now())
                .build();

        ScrapingJobResult savedResult = scrapingJobResultRepository.save(scrapingJobResult);

        return mapper.mapTo(savedResult);
    }

    // READ METHODS
    public Optional<ScrapingJobResultDTO> findResult(Long id) {
        return scrapingJobResultRepository.findById(id)
                .map(mapper::mapTo);
    }

    public List<ScrapingJobResultDTO> findResults() {
        return scrapingJobResultRepository.findAll().stream()
                .map(mapper::mapTo)
                .toList();
    }

    public List<ScrapingJobResultDTO> findResultsByProductType(String productType) {
        return scrapingJobResultRepository.findScrapingJobsByProduct(productType).stream()
                .map(mapper::mapTo)
                .toList();
    }

    public List<ScrapingJobResultDTO> findResultsByVendor(String vendor) {
        return scrapingJobResultRepository.findScrapingJobsByVendor(vendor).stream()
                .map(mapper::mapTo)
                .toList();
    }
}