package com.price_tracker.controllers;

import com.price_tracker.domain.dto.GPUWorkstationDTO;
import com.price_tracker.services.GPUWorkstationService;
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
public class GPUWorkstationController {

    private final GPUWorkstationService gpuWorkstationService;

    // gpu create-one endpoint
    @PostMapping(path = "/workstaion_gpus")
    public ResponseEntity<GPUWorkstationDTO> createWorkstationGPU(
            @RequestBody final GPUWorkstationDTO gpuWorkstationDTO) {

        log.info("Got GPU: {}" + gpuWorkstationDTO.toString());
        GPUWorkstationDTO savedGPUWorkstation = gpuWorkstationService.save(gpuWorkstationDTO);
        return new ResponseEntity<>(savedGPUWorkstation, HttpStatus.CREATED);
    }

    // gpu create-all endpoint
    @PostMapping(path = "/workstaion_gpus/saveall")
    public ResponseEntity<List<GPUWorkstationDTO>> createWorkstationGPU(
            @RequestBody final List<GPUWorkstationDTO> gpuWorkstationDTOS) {

        log.info("Processing batch of " + gpuWorkstationDTOS.size() + " GPU WS records.");
        List<GPUWorkstationDTO> savedEntities = gpuWorkstationService.saveAll(gpuWorkstationDTOS);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    // workstation gpu read-all endpoint
    @GetMapping(path = "/workstation_gpus")
    public ResponseEntity<List<GPUWorkstationDTO>> listWorkstationGPUs() {
        return new ResponseEntity<>(gpuWorkstationService.findAll(), HttpStatus.OK);
    }

    // workstation gpu read-one endpoint
    @GetMapping(path = "/workstation_gpus/{id}")
    public ResponseEntity<GPUWorkstationDTO> getWorkstationGPU(@PathVariable String id) {

        Optional<GPUWorkstationDTO> foundGPU = gpuWorkstationService.findOne(id);

        return foundGPU.map(gpu ->
                new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // workstation gpu full-update endpoint
    @PutMapping(path = "/workstation_gpus/{id}")
    public ResponseEntity<GPUWorkstationDTO> fullUpdate(
            @PathVariable String id,
            @RequestBody GPUWorkstationDTO gpuWorkstationDTO) {

        return gpuWorkstationService.fullUpdate(id, gpuWorkstationDTO)
                .map(gpu ->
                        new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // workstation gpu partial-update endpoint
    @PatchMapping(path = "/workstation_gpus/{id}")
    public ResponseEntity<GPUWorkstationDTO> partialUpdate(
            @PathVariable String id,
            @RequestBody GPUWorkstationDTO gpuWorkstationDTO) {

        return gpuWorkstationService.partialUpdate(id, gpuWorkstationDTO)
                .map(gpu ->
                        new ResponseEntity<>(gpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // workstation gpu delete endpoint
    @DeleteMapping(path = "/workstation_gpus/{id}")
    public ResponseEntity<GPUWorkstationDTO> deleteWorkstationGPU(@PathVariable String id) {
        gpuWorkstationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
