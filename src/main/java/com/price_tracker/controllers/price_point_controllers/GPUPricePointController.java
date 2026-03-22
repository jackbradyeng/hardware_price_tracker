package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import com.price_tracker.services.price_point_services.GPUPricePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log
public class GPUPricePointController {

    private final GPUPricePointService gpuPricePointService;

    // gpu price point read-all endpoint
    @GetMapping(path = "/api/gpu_pricepoints")
    public ResponseEntity<List<GPUPricePointDTO>> listGPUPricePoints() {
        return new ResponseEntity<>(gpuPricePointService.findAll(), HttpStatus.OK);
    }

    // gpu get price-points and gpu data by model number
    @GetMapping(path = "/api/gpu_pricepoints/{modelNumber}")
    public ResponseEntity<List<GPUDataAndPricePointDTO>> findGPUPricePointsByModelNumber(
            @RequestParam String modelNumber) {
        return new ResponseEntity<>(gpuPricePointService.findByModelNumber(modelNumber), HttpStatus.OK);
    }

    // gpu price point read-one endpoint
    @GetMapping(path = "/api/gpu_pricepoints/{id}")
    public ResponseEntity<GPUPricePointDTO> getGPUPricePoint(@PathVariable Long id) {

        Optional<GPUPricePointDTO> foundGPU = gpuPricePointService.findOne(id);

        return foundGPU.map(gpu ->
                new ResponseEntity<>(gpu, HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
