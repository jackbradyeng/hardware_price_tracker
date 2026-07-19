package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.services.price_point_services.GenericPricePointService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
public class CPUPricePointController {

    private final GenericPricePointService<CPUDataAndPricePointDTO> cpuPricePointService;

    @GetMapping(path = "/api/v1/cpu_pricepoints")
    public ResponseEntity<Page<GenericPricePointDTO>> listCPUPricePoints(
            @PageableDefault(size = 30) Pageable pageable) {
        return new ResponseEntity<>(cpuPricePointService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/cpu_pricepoints/{modelNumber}")
    public ResponseEntity<CPUDataAndPricePointDTO> findCPUPricePointsByModelNumber(
            @NotBlank @PathVariable String modelNumber,
            @PageableDefault(size = 30) Pageable pageable) {

        Optional<CPUDataAndPricePointDTO> cpuPricePointDTOS = cpuPricePointService
                .findByModelNumber(modelNumber, pageable);

        return cpuPricePointDTOS.map(foundPriceHistory ->
                new ResponseEntity<>(foundPriceHistory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}