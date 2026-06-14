package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.GPUWorkstationDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.services.price_point_services.GPUWorkstationPricePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
@Log
public class GPUWorkstationPricePointController {

    private final GPUWorkstationPricePointService gpuWorkstationPricePointService;

    @GetMapping(path = "/api/workstation_gpu_pricepoints")
    public ResponseEntity<Page<GenericPricePointDTO>> listWorkstationGPUPricePoints(
            @PageableDefault(size = 30) Pageable pageable) {
        return new ResponseEntity<>(gpuWorkstationPricePointService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/api/workstation_gpu_pricepoints/{modelNumber}")
    public ResponseEntity<GPUWorkstationDataAndPricePointDTO> findWorkstationGPUPricePointsByModelNumber(
            @PathVariable String modelNumber,
            @PageableDefault(size = 30) Pageable pageable) {

        Optional<GPUWorkstationDataAndPricePointDTO> pricePointsDTOS = gpuWorkstationPricePointService
                .findByModelNumber(modelNumber, pageable);

        return pricePointsDTOS.map(foundPriceHistory ->
                new ResponseEntity<>(foundPriceHistory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}