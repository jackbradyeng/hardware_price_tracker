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

/** Fully functional REST API with CRUD functionality. */
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
        GPU savedGPU = gpuService.save(gpu);
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

    // gpu update endpoint
    @PutMapping(path = "/gpus/{id}")
    public ResponseEntity<GPUDTO> fullUpdateGPU(
            @PathVariable("id") String id,
            @RequestBody GPUDTO gpuDTO) {
        if(!gpuService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        gpuDTO.setModelNumber(id);
        GPU gpu = gpuMapper.mapFrom(gpuDTO);
        GPU savedGPU = gpuService.save(gpu);
        return new ResponseEntity<>(
                gpuMapper.mapTo(savedGPU),
                HttpStatus.OK
        );
    }

    // gpu partial-update endpoint
    @PatchMapping(path = "/gpus/{id}")
    public ResponseEntity<GPUDTO> partialUpdate(
            @PathVariable("id") String id,
            @RequestBody GPUDTO gpuDTO
    ) {
        if(!gpuService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GPU gpu = gpuMapper.mapFrom(gpuDTO);
        GPU updatedGPU = gpuService.partialUpdate(id, gpu);
        return new ResponseEntity<>(
                gpuMapper.mapTo(updatedGPU),
                HttpStatus.OK
        );
    }

    // gpu delete endpoint
    @DeleteMapping(path = "/gpus/{id}")
    public ResponseEntity<GPUDTO> deleteGPU(@PathVariable("id") String id) {
        gpuService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
