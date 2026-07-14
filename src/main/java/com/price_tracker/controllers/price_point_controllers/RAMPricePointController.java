package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.services.price_point_services.GenericPricePointService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RAMPricePointController {

    private final GenericPricePointService<RAMDataAndPricePointDTO> ramPricePointService;

    @GetMapping(path = "/api/ram_pricepoints")
    public ResponseEntity<Page<GenericPricePointDTO>> listRAMPricePoints(
            @PageableDefault(size = 30) Pageable pageable) {
        return new ResponseEntity<>(ramPricePointService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/api/ram_pricepoints/{modelNumber}")
    public ResponseEntity<RAMDataAndPricePointDTO> findRAMPricePointsBYModelNumber(
            @NotBlank @PathVariable String modelNumber,
            @PageableDefault(size = 30) Pageable pageable) {

        Optional<RAMDataAndPricePointDTO> pricePointDTOS = ramPricePointService
                .findByModelNumber(modelNumber, pageable);

        return pricePointDTOS.map(foundPriceHistory ->
                new ResponseEntity<>(foundPriceHistory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}