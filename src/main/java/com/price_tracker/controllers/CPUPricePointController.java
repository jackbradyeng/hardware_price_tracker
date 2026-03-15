package com.price_tracker.controllers;

import com.price_tracker.domain.dto.CPUPricePointDTO;
import com.price_tracker.services.price_points.CPUPricePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log
public class CPUPricePointController {

    private final CPUPricePointService cpuPricePointService;

    // cpu price point read-all endpoint
    @GetMapping(path = "/cpu_pricepoints")
    public ResponseEntity<List<CPUPricePointDTO>> listCPUPricePoints() {
        return new ResponseEntity<>(cpuPricePointService.findAll(), HttpStatus.OK);
    }

    // cpu price point read-one endpoint
    @GetMapping(path = "/cpu_pricepoints/{id}")
    public ResponseEntity<CPUPricePointDTO> getCPUPricePoint(@PathVariable Long id) {

        Optional<CPUPricePointDTO> foundCPU = cpuPricePointService.findOne(id);

        return foundCPU.map(cpu ->
                 new ResponseEntity<>(cpu, HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
