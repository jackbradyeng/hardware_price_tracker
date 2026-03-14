package com.price_tracker.controllers;

import com.price_tracker.domain.dto.GPUWorkstationPricePointDTO;
import com.price_tracker.services.GPUWorkstationPricePointService;
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
    @GetMapping(path = "/workstation_gpu_pricepoints")
    public ResponseEntity<List<GPUWorkstationPricePointDTO>> listWorkstationGPUPricePoints() {
        return new ResponseEntity<>(gpuWorkstationPricePointService.findAll(), HttpStatus.OK);
    }

    // workstation gpu price point read-one endpoint
    @GetMapping(path = "/workstation_gpu_pricepoints/{id}")
    public ResponseEntity<GPUWorkstationPricePointDTO> getWorkstationGPUPricePoint(@PathVariable Long id) {

        Optional<GPUWorkstationPricePointDTO> foundGPU = gpuWorkstationPricePointService.findOne(id);

        return foundGPU.map(gpu ->
                        new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
