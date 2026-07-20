package com.priceTracker.controllers.productControllers;

import com.priceTracker.domain.dto.productDTOs.NVMEDTO;
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
public class NVMEController {

    private final GenericProductService<NVMEDTO> nvmeService;

    @PostMapping(path = "/api/v1/nvmes")
    public ResponseEntity<NVMEDTO> createNVME(@Valid @RequestBody final NVMEDTO nvmeDTO) {
        log.info("Got NVME: " + nvmeDTO.toString());
        NVMEDTO savedNVME = nvmeService.save(nvmeDTO);
        return new ResponseEntity<>(savedNVME, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/v1/nvmes/saveall")
    public ResponseEntity<List<NVMEDTO>> createNVMEs(@Valid @RequestBody final List<NVMEDTO> nvmeDTOs) {
        log.info("Processing batch of " + nvmeDTOs.size() + " NVME records");
        List<NVMEDTO> savedEntities = nvmeService.saveAll(nvmeDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/nvmes")
    public ResponseEntity<List<NVMEDTO>> listNVMEs() {
        return new ResponseEntity<>(nvmeService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/nvmes/{id}")
    public ResponseEntity<NVMEDTO> getNVME(@NotBlank @PathVariable String id) {
        Optional<NVMEDTO> foundNVME = nvmeService.findOne(id);
        return foundNVME.map(nvme -> new ResponseEntity<>(nvme, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/v1/nvmes/{id}")
    public ResponseEntity<NVMEDTO> fullUpdateNVME(@NotBlank @PathVariable String id, @Valid @RequestBody NVMEDTO nvmeDTO) {
        return nvmeService.fullUpdate(id, nvmeDTO)
                .map(nvme -> new ResponseEntity<>(nvme, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/v1/nvmes/{id}")
    public ResponseEntity<NVMEDTO> partialUpdate(@NotBlank @PathVariable String id, @Valid @RequestBody NVMEDTO nvmeDTO) {
        return nvmeService.partialUpdate(id, nvmeDTO)
                .map(nvme -> new ResponseEntity<>(nvme, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/v1/nvmes/{id}")
    public ResponseEntity<NVMEDTO> deleteNVME(@NotBlank @PathVariable String id) {
        nvmeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}