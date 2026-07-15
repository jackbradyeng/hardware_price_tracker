package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.SSDDataAndPricePointDTO;
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
public class SSDPricePointController {

    private final GenericPricePointService<SSDDataAndPricePointDTO> ssdPricePointService;

    @GetMapping(path = "/api/ssd_pricepoints")
    public ResponseEntity<Page<GenericPricePointDTO>> listSSDPricePoints(
            @PageableDefault(size = 30) Pageable pageable) {
        return new ResponseEntity<>(ssdPricePointService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/api/ssd_pricepoints/{modelNumber}")
    public ResponseEntity<SSDDataAndPricePointDTO> findSSDPricePointsByModelNumber(
            @NotBlank @PathVariable String modelNumber,
            @PageableDefault(size = 30) Pageable pageable) {

        Optional<SSDDataAndPricePointDTO> ssdPricePointDTOS = ssdPricePointService
                .findByModelNumber(modelNumber, pageable);

        return ssdPricePointDTOS.map(foundPriceHistory ->
                new ResponseEntity<>(foundPriceHistory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}