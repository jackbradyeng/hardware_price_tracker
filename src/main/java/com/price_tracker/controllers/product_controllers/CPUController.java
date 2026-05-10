package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.services.product_services.CPUService;
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
public class CPUController {

    private final CPUService cpuService;

    @PostMapping(path = "/api/cpus")
    public ResponseEntity<CPUDTO> createCPU(@RequestBody final CPUDTO cpuDTO) {
        log.info("Got CPU: {}" + cpuDTO.toString());
        CPUDTO savedCPU = cpuService.save(cpuDTO);
        return new ResponseEntity<>(savedCPU, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/cpus/saveall")
    public ResponseEntity<List<CPUDTO>> createCPU(@RequestBody final List<CPUDTO> cpuDTOs) {
        log.info("Processing batch of " + cpuDTOs.size() + " CPU records.");
        List<CPUDTO> savedEntities = cpuService.saveAll(cpuDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/cpus")
    public ResponseEntity<List<CPUDTO>> listCPUs() {
        return new ResponseEntity<>(cpuService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/cpus/{id}")
    public ResponseEntity<CPUDTO> getCPU(@PathVariable String id) {
        Optional<CPUDTO> foundCPU = cpuService.findOne(id);
        return foundCPU.map(cpu -> new ResponseEntity<>(cpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/cpus/{id}")
    public ResponseEntity<CPUDTO> fullUpdateCPU(@PathVariable String id, @RequestBody CPUDTO cpuDTO) {
        return cpuService.fullUpdate(id, cpuDTO)
                .map(cpu -> new ResponseEntity<>(cpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/cpus/{id}")
    public ResponseEntity<CPUDTO> partialUpdate(@PathVariable String id, @RequestBody CPUDTO cpuDTO) {
        return cpuService.partialUpdate(id, cpuDTO)
                .map(cpu -> new ResponseEntity<>(cpu, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/cpus/{id}")
    public ResponseEntity<CPUDTO> deleteCPU(@PathVariable String id) {
        cpuService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}