package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.services.product_services.GenericProductService;
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
public class CPUController {

    private final GenericProductService<CPUDTO> cpuService;

    @PostMapping(path = "/api/v1/cpus")
    public ResponseEntity<CPUDTO> createCPU(@Valid @RequestBody final CPUDTO cpuDTO) {
        log.info("Got CPU: " + cpuDTO.toString());
        CPUDTO savedCPU = cpuService.save(cpuDTO);
        return new ResponseEntity<>(savedCPU, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/v1/cpus/saveall")
    public ResponseEntity<List<CPUDTO>> createCPU(@Valid @RequestBody final List<CPUDTO> cpuDTOs) {
        log.info("Processing batch of " + cpuDTOs.size() + " CPU records.");
        List<CPUDTO> savedEntities = cpuService.saveAll(cpuDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/cpus")
    public ResponseEntity<List<CPUDTO>> listCPUs() {
        return new ResponseEntity<>(cpuService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/cpus/{id}")
    public ResponseEntity<CPUDTO> getCPU(@NotBlank @PathVariable String id) {
        Optional<CPUDTO> foundCPU = cpuService.findOne(id);
        return foundCPU.map(cpu -> new ResponseEntity<>(cpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/v1/cpus/{id}")
    public ResponseEntity<CPUDTO> fullUpdateCPU(@NotBlank @PathVariable String id,
                                                @Valid @RequestBody CPUDTO cpuDTO) {
        return cpuService.fullUpdate(id, cpuDTO)
                .map(cpu -> new ResponseEntity<>(cpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/v1/cpus/{id}")
    public ResponseEntity<CPUDTO> partialUpdate(@NotBlank @PathVariable String id,
                                                @Valid @RequestBody CPUDTO cpuDTO) {
        return cpuService.partialUpdate(id, cpuDTO)
                .map(cpu -> new ResponseEntity<>(cpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/v1/cpus/{id}")
    public ResponseEntity<CPUDTO> deleteCPU(@NotBlank @PathVariable String id) {
        cpuService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}