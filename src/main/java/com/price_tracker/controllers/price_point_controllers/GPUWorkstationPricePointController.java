package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.GPUWorkstationDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GPUWorkstationPricePointDTO;
import com.price_tracker.services.price_point_services.GPUWorkstationPricePointService;
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
public class GPUWorkstationPricePointController {

    private final GPUWorkstationPricePointService gpuWorkstationPricePointService;

    // workstation gpu price point read-all endpoint
    @GetMapping(path = "/api/workstation_gpu_pricepoints")
    public ResponseEntity<List<GPUWorkstationPricePointDTO>> listWorkstationGPUPricePoints() {
        return new ResponseEntity<>(gpuWorkstationPricePointService.findAll(), HttpStatus.OK);
    }

    // workstation gpu get price-points and gpu data by model number
    @GetMapping(path = "/api/workstation_gpu_pricepoints/{modelNumber}")
    public ResponseEntity<GPUWorkstationDataAndPricePointDTO> findWorkstationGPUPricePointsByModelNumber(
            @PathVariable String modelNumber) {

        Optional<GPUWorkstationDataAndPricePointDTO> pricePointsDTOS = gpuWorkstationPricePointService
                .findByModelNumber(modelNumber);

        return pricePointsDTOS.map(foundPriceHistory ->
                new ResponseEntity<>(foundPriceHistory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}