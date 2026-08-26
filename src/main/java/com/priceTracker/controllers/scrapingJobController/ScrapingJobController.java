package com.priceTracker.controllers.scrapingJobController;

import com.priceTracker.domain.dto.scrapingJobDTOs.ScrapingJobResultDTO;
import com.priceTracker.services.pricePointServices.ScrapingJobService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
public class ScrapingJobController {

    private final ScrapingJobService scrapingJobService;

    // READ ENDPOINTS (PUBLIC)
    @GetMapping(path = "/api/v1/scraping-job-results")
    public ResponseEntity<List<ScrapingJobResultDTO>> findAll() {

        Optional<List<ScrapingJobResultDTO>> results = scrapingJobService.findAll();

        return results.map(foundResults ->
                new ResponseEntity<>(foundResults, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/api/v1/scraping-job-results/{id}")
    public ResponseEntity<ScrapingJobResultDTO> findOne(@NotNull @PathVariable Long id) {

        Optional<ScrapingJobResultDTO> result = scrapingJobService.findResult(id);

        return result.map(foundResult ->
                new ResponseEntity<>(foundResult, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/api/v1/scraping-job-results/products/{productType}")
    public ResponseEntity<List<ScrapingJobResultDTO>> findByProduct(@NotBlank @PathVariable String productType) {

        Optional<List<ScrapingJobResultDTO>> results = scrapingJobService.findByProductType(productType);

        return results.map(foundResults ->
                new ResponseEntity<>(foundResults, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/api/v1/scraping-job-results/vendors/{vendor}")
    public ResponseEntity<List<ScrapingJobResultDTO>> findByVendor(@NotBlank @PathVariable String vendor) {

        Optional<List<ScrapingJobResultDTO>> results = scrapingJobService.findByVendor(vendor);

        return results.map(foundResults ->
                new ResponseEntity<>(foundResults, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}