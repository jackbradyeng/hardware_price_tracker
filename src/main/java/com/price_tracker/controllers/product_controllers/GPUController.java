package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.services.product_services.GenericProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log
public class GPUController {

    private final GenericProductService<GPUDTO> gpuService;

    @PostMapping(path = "/api/gpus")
    public ResponseEntity<GPUDTO> createGPU(@Valid @RequestBody final GPUDTO gpuDTO) {
        log.info("Got GPU: " + gpuDTO);
        GPUDTO savedGPU = gpuService.save(gpuDTO);
        return new ResponseEntity<>(savedGPU, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/gpus/saveall")
    public ResponseEntity<List<GPUDTO>> createGPU(@Valid @RequestBody final List<GPUDTO> gpuDTOs) {
        log.info("Processing batch of " + gpuDTOs.size() + " GPU records.");
        List<GPUDTO> savedEntities = gpuService.saveAll(gpuDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/gpus")
    public ResponseEntity<List<GPUDTO>> listGPUs() {
        return new ResponseEntity<>(gpuService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/gpus/{id}")
    public ResponseEntity<GPUDTO> getGPU(@NotBlank @PathVariable String id) {
        Optional<GPUDTO> foundGPU = gpuService.findOne(id);
        return foundGPU.map(gpu -> new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/gpus/{id}")
    public ResponseEntity<GPUDTO> fullUpdateGPU(@NotBlank @PathVariable String id, @Valid @RequestBody GPUDTO gpuDTO) {
        return gpuService.fullUpdate(id, gpuDTO)
                .map(gpu -> new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/gpus/{id}")
    public ResponseEntity<GPUDTO> partialUpdate(@NotBlank @PathVariable String id, @Valid @RequestBody GPUDTO gpuDTO) {
        return gpuService.partialUpdate(id, gpuDTO)
                .map(gpu -> new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/gpus/{id}")
    public ResponseEntity<GPUDTO> deleteGPU(@NotBlank @PathVariable String id) {
        gpuService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}