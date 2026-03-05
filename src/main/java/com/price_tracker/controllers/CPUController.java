package com.price_tracker.controllers;

import com.price_tracker.domain.dto.CPUDTO;
import com.price_tracker.domain.entities.CPUEntity;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.services.CPUService;
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
    private final Mapper<CPUEntity, CPUDTO> cpuMapper;

    // cpu create endpoint
    @PostMapping(name = "/cpus")
    public ResponseEntity<CPUDTO> createCPU(@RequestBody final CPUDTO cpuDTO) {
        log.info("Got CPU: {}" + cpuDTO.toString());
        CPUEntity cpuEntity = cpuMapper.mapFrom(cpuDTO);
        CPUEntity savedCPU = cpuService.save(cpuEntity);
        return new ResponseEntity<>(cpuMapper.mapTo(savedCPU), HttpStatus.CREATED);
    }

    // cpu read-all endpoint
    @GetMapping(path = "/cpus")
    public ResponseEntity<List<CPUDTO>> listCPUs() {
        List<CPUEntity> cpus = cpuService.findAll();
        return new ResponseEntity<>(cpus.stream().map(cpuMapper::mapTo).toList(), HttpStatus.OK);
    }

    // cpu read-one endpoint
    @GetMapping(path = "/cpus/{id}")
    public ResponseEntity<CPUDTO> getCPU(@PathVariable String id) {
        Optional<CPUEntity> foundCPU = cpuService.findOne(id);
        return foundCPU.map(cpu -> {
            CPUDTO cpudto = cpuMapper.mapTo(cpu);
            return new ResponseEntity<>(cpudto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // cpu update endpoint

    // cpu delete endpoint

}
