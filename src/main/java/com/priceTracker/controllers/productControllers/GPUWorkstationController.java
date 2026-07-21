package com.priceTracker.controllers.productControllers;

import com.priceTracker.domain.dto.productDTOs.GPUWorkstationDTO;
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
public class GPUWorkstationController {

    private final GenericProductService<GPUWorkstationDTO> gpuWorkstationService;

    @PostMapping(path = "/api/v1/workstation_gpus")
    public ResponseEntity<GPUWorkstationDTO> createWorkstationGPU(
            @Valid @RequestBody final GPUWorkstationDTO gpuWorkstationDTO) {

        log.info("Got GPU: " + gpuWorkstationDTO.toString());
        GPUWorkstationDTO savedGPUWorkstation = gpuWorkstationService.save(gpuWorkstationDTO);
        return new ResponseEntity<>(savedGPUWorkstation, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/v1/workstation_gpus/saveall")
    public ResponseEntity<List<GPUWorkstationDTO>> createWorkstationGPU(
            @Valid @RequestBody final List<GPUWorkstationDTO> gpuWorkstationDTOS) {

        log.info("Processing batch of " + gpuWorkstationDTOS.size() + " GPU WS records.");
        List<GPUWorkstationDTO> savedEntities = gpuWorkstationService.saveAll(gpuWorkstationDTOS);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/workstation_gpus")
    public ResponseEntity<List<GPUWorkstationDTO>> listWorkstationGPUs() {
        return new ResponseEntity<>(gpuWorkstationService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/workstation_gpus/{id}")
    public ResponseEntity<GPUWorkstationDTO> getWorkstationGPU(@NotBlank @PathVariable String id) {

        Optional<GPUWorkstationDTO> foundGPU = gpuWorkstationService.findOne(id);

        return foundGPU.map(gpu ->
                new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/v1/workstation_gpus/{id}")
    public ResponseEntity<GPUWorkstationDTO> fullUpdate(
            @NotBlank @PathVariable String id,
            @Valid @RequestBody GPUWorkstationDTO gpuWorkstationDTO) {

        return gpuWorkstationService.fullUpdate(id, gpuWorkstationDTO)
                .map(gpu ->
                        new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/v1/workstation_gpus/{id}")
    public ResponseEntity<GPUWorkstationDTO> partialUpdate(
            @NotBlank @PathVariable String id,
            @Valid @RequestBody GPUWorkstationDTO gpuWorkstationDTO) {

        return gpuWorkstationService.partialUpdate(id, gpuWorkstationDTO)
                .map(gpu ->
                        new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/v1/workstation_gpus/{id}")
    public ResponseEntity<GPUWorkstationDTO> deleteWorkstationGPU(@NotBlank @PathVariable String id) {
        gpuWorkstationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}