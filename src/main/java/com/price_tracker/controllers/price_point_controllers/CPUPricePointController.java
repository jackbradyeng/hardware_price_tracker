package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.CPUPricePointDTO;
import com.price_tracker.services.price_point_services.CPUPricePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log
public class CPUPricePointController {

    private final CPUPricePointService cpuPricePointService;

    // cpu price point read-all endpoint
    @GetMapping(path = "/api/cpu_pricepoints")
    public ResponseEntity<List<CPUPricePointDTO>> listCPUPricePoints() {
        return new ResponseEntity<>(cpuPricePointService.findAll(), HttpStatus.OK);
    }

    // cpu get price-points and cpu data by model number
    @GetMapping(path = "/api/cpu_pricepoints/{modelNumber}")
    public ResponseEntity<CPUDataAndPricePointDTO> findCPUPricePointsByModelNumber(
            @PathVariable String modelNumber) {
        return new ResponseEntity<>(cpuPricePointService.findByModelNumber(modelNumber), HttpStatus.OK);
    }
}
