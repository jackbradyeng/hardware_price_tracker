package com.priceTracker.domain.dto.scrapingJobDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapingJobResultDTO {

    private Long id;
    @NotBlank
    private String vendor;
    @NotBlank
    private String productType;
    @NotNull
    private Integer recordsReceived;
    @NotNull
    private Integer recordsSaved;
    @NotNull
    private LocalDateTime timeCompleted;
}