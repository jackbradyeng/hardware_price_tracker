package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.services.product_services.GPUService;
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

    private final GPUService gpuService;
    private final Mapper<GPUEntity, GPUDTO> gpuMapper;

    @PostMapping(path = "/api/gpus")
    public ResponseEntity<GPUDTO> createGPU(@RequestBody final GPUDTO gpuDTO) {
        log.info("Got GPU: {}" + gpuDTO.toString());
        GPUEntity gpuEntity = gpuMapper.mapFrom(gpuDTO);
        GPUEntity savedGPUEntity = gpuService.save(gpuEntity);
        return new ResponseEntity<>(gpuMapper.mapTo(savedGPUEntity), HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/gpus/saveall")
    public ResponseEntity<List<GPUDTO>> createGPU(@RequestBody final List<GPUDTO> gpuDTOs) {
        log.info("Processing batch of " + gpuDTOs.size() + " GPU records.");

        List<GPUEntity> gpuEntities = gpuDTOs.stream()
                .map(gpuMapper::mapFrom)
                .toList();

        List<GPUEntity> savedEntities = gpuService.saveAll(gpuEntities);

        List<GPUDTO> responseList = savedEntities.stream()
                .map(gpuMapper::mapTo)
                .toList();

        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/gpus")
    public ResponseEntity<List<GPUDTO>> listGPUs() {
        List<GPUEntity> gpus = gpuService.findAll();
        return new ResponseEntity<>(gpus.stream().map(gpuMapper::mapTo).toList(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/gpus/{id}")
    public ResponseEntity<GPUDTO> getGPU(@PathVariable String id) {
        Optional<GPUEntity> foundGPU = gpuService.findOne(id);
        return foundGPU.map(gpu -> {
            GPUDTO gpudto = gpuMapper.mapTo(gpu);
            return new ResponseEntity<>(gpudto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/gpus/{id}")
    public ResponseEntity<GPUDTO> fullUpdateGPU(@PathVariable String id, @RequestBody GPUDTO gpuDTO) {
        if(!gpuService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        gpuDTO.setModelNumber(id);
        GPUEntity gpuEntity = gpuMapper.mapFrom(gpuDTO);
        GPUEntity savedGPUEntity = gpuService.save(gpuEntity);
        return new ResponseEntity<>(gpuMapper.mapTo(savedGPUEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/api/gpus/{id}")
    public ResponseEntity<GPUDTO> partialUpdate(@PathVariable String id, @RequestBody GPUDTO gpuDTO) {
        if(!gpuService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GPUEntity gpuEntity = gpuMapper.mapFrom(gpuDTO);
        GPUEntity updatedGPUEntity = gpuService.partialUpdate(id, gpuEntity);
        return new ResponseEntity<>(gpuMapper.mapTo(updatedGPUEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/gpus/{id}")
    public ResponseEntity<GPUDTO> deleteGPU(@PathVariable String id) {
        gpuService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
