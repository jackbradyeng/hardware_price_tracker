package com.price_tracker.controllers;

import com.price_tracker.domain.entities.GPU;
import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.mappers.impl.GPUMapper;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.services.GPUService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

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

    // gpu read endpoint
    @GetMapping(path = "/gpus")
    public GPU retrieveGPU() {
        return GPU.builder()
                .modelNumber("PRIME-RTX5070TI-O16G")
                .name("Asus Prime GeForce RTX 5070 Ti OC 16G Graphics Card")
                .chipManufacturer("NVIDIA")
                .boardManufacturer("ASUS")
                .videoMemory(16)
                .price(new BigDecimal("1598.00"))
                .build();
    }
}
