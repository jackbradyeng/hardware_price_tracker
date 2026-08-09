package com.priceTracker.domain.entities.scrapingJobEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.priceTracker.constants.DatabaseConstants.SCRAPING_JOB_RESULT;
import static com.priceTracker.constants.DatabaseConstants.SCRAPING_JOB_RESULT_SEQUENCE;

/**
 * Stores the result of a scraping job for a particular product and a particular vendor at a point in time.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = SCRAPING_JOB_RESULT)
public class ScrapingJobResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SCRAPING_JOB_RESULT_SEQUENCE)
    @SequenceGenerator(
            name = SCRAPING_JOB_RESULT_SEQUENCE,
            sequenceName = SCRAPING_JOB_RESULT_SEQUENCE,
            allocationSize = 1
    )
    private Long id;
    private String vendor;
    private String productType;
    private Integer recordsReceived;
    private Integer recordsSaved;
    private LocalDateTime timeCompleted;
}