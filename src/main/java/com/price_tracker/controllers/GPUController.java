package com.price_tracker.controllers;

import com.price_tracker.domain.entities.GPU;
import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.mappers.impl.GPUMapper;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.services.GPUService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@Log
public class GPUController {

    private final GPUService gpuService;
    private final Mapper<GPU, GPUDTO> gpuMapper;

    // gpu service dependency injection
    public GPUController(GPUService gpuService, GPUMapper gpuMapper) {
        this.gpuService = gpuService;
        this.gpuMapper = gpuMapper;
    }

    // gpu create endpoint
    @PostMapping(path = "/gpus")
    public ResponseEntity<GPUDTO> createGPU(@RequestBody final GPUDTO gpuDTO) {
        log.info("Got GPU: {}" + gpuDTO.toString());
        GPU gpu = gpuMapper.mapFrom(gpuDTO);
        GPU savedGPU = gpuService.createGPU(gpu);
        return new ResponseEntity<>(gpuMapper.mapTo(savedGPU), HttpStatus.CREATED);
    }

    // gpu read-all endpoint
    @GetMapping(path = "/gpus")
    public List<GPUDTO> listGPUs() {
        List<GPU> gpus = gpuService.findAll();
        return gpus.stream()
                .map(gpuMapper::mapTo)
                .toList();
    }

    // gpu get-one endpoint
    @GetMapping(path = "/gpus/{id}")
    public ResponseEntity<GPUDTO> getGPU(@PathVariable("id") String id) {
        Optional<GPU> foundGPU = gpuService.findOne(id);
        return foundGPU.map(gpu -> {
            GPUDTO gpudto = gpuMapper.mapTo(gpu);
            return new ResponseEntity<>(gpudto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
