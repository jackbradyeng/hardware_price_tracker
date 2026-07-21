package com.priceTracker.controllers.productControllers;

import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.services.productServices.GenericProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Log
@Validated
@RestController
@RequiredArgsConstructor
public class GPUController {

    private final GenericProductService<GPUDTO> gpuService;

    @PostMapping(path = "/api/v1/gpus")
    public ResponseEntity<GPUDTO> createGPU(@Valid @RequestBody final GPUDTO gpuDTO) {
        log.info("Got GPU: " + gpuDTO);
        GPUDTO savedGPU = gpuService.save(gpuDTO);
        return new ResponseEntity<>(savedGPU, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/v1/gpus/saveall")
    public ResponseEntity<List<GPUDTO>> createGPU(@Valid @RequestBody final List<GPUDTO> gpuDTOs) {
        log.info("Processing batch of " + gpuDTOs.size() + " GPU records.");
        List<GPUDTO> savedEntities = gpuService.saveAll(gpuDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/gpus")
    public ResponseEntity<List<GPUDTO>> listGPUs() {
        return new ResponseEntity<>(gpuService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/gpus/{id}")
    public ResponseEntity<GPUDTO> getGPU(@NotBlank @PathVariable String id) {
        Optional<GPUDTO> foundGPU = gpuService.findOne(id);
        return foundGPU.map(gpu -> new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/v1/gpus/{id}")
    public ResponseEntity<GPUDTO> fullUpdateGPU(@NotBlank @PathVariable String id, @Valid @RequestBody GPUDTO gpuDTO) {
        return gpuService.fullUpdate(id, gpuDTO)
                .map(gpu -> new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/v1/gpus/{id}")
    public ResponseEntity<GPUDTO> partialUpdate(@NotBlank @PathVariable String id, @Valid @RequestBody GPUDTO gpuDTO) {
        return gpuService.partialUpdate(id, gpuDTO)
                .map(gpu -> new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/v1/gpus/{id}")
    public ResponseEntity<GPUDTO> deleteGPU(@NotBlank @PathVariable String id) {
        gpuService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}