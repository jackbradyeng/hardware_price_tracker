package com.price_tracker.controllers;

import com.price_tracker.domain.entities.GPUEntity;
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
    private final Mapper<GPUEntity, GPUDTO> gpuMapper;

    // gpu service dependency injection
    public GPUController(GPUService gpuService, GPUMapper gpuMapper) {
        this.gpuService = gpuService;
        this.gpuMapper = gpuMapper;
    }

    // gpu create endpoint
    @PostMapping(path = "/gpus")
    public ResponseEntity<GPUDTO> createGPU(@RequestBody final GPUDTO gpuDTO) {
        log.info("Got GPU: {}" + gpuDTO.toString());
        GPUEntity gpuEntity = gpuMapper.mapFrom(gpuDTO);
        GPUEntity savedGPUEntity = gpuService.save(gpuEntity);
        return new ResponseEntity<>(gpuMapper.mapTo(savedGPUEntity), HttpStatus.CREATED);
    }

    // gpu read-all endpoint
    @GetMapping(path = "/gpus")
    public List<GPUDTO> listGPUs() {
        List<GPUEntity> gpuses = gpuService.findAll();
        return gpuses.stream()
                .map(gpuMapper::mapTo)
                .toList();
    }

    // gpu get-one endpoint
    @GetMapping(path = "/gpus/{id}")
    public ResponseEntity<GPUDTO> getGPU(@PathVariable("id") String id) {
        Optional<GPUEntity> foundGPU = gpuService.findOne(id);
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
        GPUEntity gpuEntity = gpuMapper.mapFrom(gpuDTO);
        GPUEntity savedGPUEntity = gpuService.save(gpuEntity);
        return new ResponseEntity<>(
                gpuMapper.mapTo(savedGPUEntity),
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
        GPUEntity gpuEntity = gpuMapper.mapFrom(gpuDTO);
        GPUEntity updatedGPUEntity = gpuService.partialUpdate(id, gpuEntity);
        return new ResponseEntity<>(
                gpuMapper.mapTo(updatedGPUEntity),
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
