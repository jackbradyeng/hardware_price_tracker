package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.HDDDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.services.price_point_services.GenericPricePointService;
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
public class HDDPricePointController {

    private final GenericPricePointService<HDDDataAndPricePointDTO> hddPricePointService;

    @GetMapping(path = "/api/hdd_pricepoints")
    public ResponseEntity<Page<GenericPricePointDTO>> listHDDPricePoints(
            @PageableDefault(size = 30) Pageable pageable) {
        return new ResponseEntity<>(hddPricePointService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/api/hdd_pricepoints/{modelNumber}")
    public ResponseEntity<HDDDataAndPricePointDTO> findHDDPricePointsByModelNumber(
            @PathVariable String modelNumber,
            @PageableDefault(size = 30) Pageable pageable) {

        Optional<HDDDataAndPricePointDTO> hddPricePointDTOS = hddPricePointService
                .findByModelNumber(modelNumber, pageable);

        return hddPricePointDTOS.map(foundPriceHistory ->
                new ResponseEntity<>(foundPriceHistory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}