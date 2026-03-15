package com.price_tracker.controllers;

import com.price_tracker.domain.dto.GPUPricePointDTO;
import com.price_tracker.services.price_points.GPUPricePointService;
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
public class GPUPricePointController {

    private final GPUPricePointService gpuPricePointService;

    // gpu price point read-all endpoint
    @GetMapping(path = "/gpu_pricepoints")
    public ResponseEntity<List<GPUPricePointDTO>> listGPUPricePoints() {
        return new ResponseEntity<>(gpuPricePointService.findAll(), HttpStatus.OK);
    }

    // gpu price point read-one endpoint
    @GetMapping(path = "/gpu_pricepoints/{id}")
    public ResponseEntity<GPUPricePointDTO> getGPUPricePoint(@PathVariable Long id) {

        Optional<GPUPricePointDTO> foundGPU = gpuPricePointService.findOne(id);

        return foundGPU.map(gpu ->
                new ResponseEntity<>(gpu, HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
