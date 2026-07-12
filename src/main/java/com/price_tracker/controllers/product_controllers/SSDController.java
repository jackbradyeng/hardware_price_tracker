package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.services.product_services.GenericProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Log
@RestController
@RequiredArgsConstructor
public class SSDController {

    private final GenericProductService<SSDDTO> ssdService;

    @PostMapping(path = "/api/ssds")
    public ResponseEntity<SSDDTO> createSSD(@Valid @RequestBody final SSDDTO ssdDTO) {
        log.info("Got SSD: " + ssdDTO.toString());
        SSDDTO savedSSD = ssdService.save(ssdDTO);
        return new ResponseEntity<>(savedSSD, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/ssds/saveall")
    public ResponseEntity<List<SSDDTO>> createSSDs(@Valid @RequestBody final List<SSDDTO> ssdDTOs) {
        log.info("Processing batch of " + ssdDTOs.size() + " SSD records");
        List<SSDDTO> savedEntities = ssdService.saveAll(ssdDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/ssds")
    public List<SSDDTO> listSSDs() {
        return ssdService.findAll();
    }

    @GetMapping(path = "/api/ssds/{id}")
    public ResponseEntity<SSDDTO> getSSD(@NotBlank @PathVariable String id) {
        Optional<SSDDTO> foundSSD = ssdService.findOne(id);
        return foundSSD.map(ssd -> new ResponseEntity<>(ssd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/ssds/{id}")
    public ResponseEntity<SSDDTO> fullUpdateSSD(@NotBlank @PathVariable String id,
                                                @Valid @RequestBody SSDDTO ssdDTO) {
        return ssdService.fullUpdate(id, ssdDTO)
                .map(ssd -> new ResponseEntity<>(ssd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/ssds/{id}")
    public ResponseEntity<SSDDTO> partialUpdate(@NotBlank @PathVariable String id,
                                                @Valid @RequestBody SSDDTO ssdDTO) {
        return ssdService.partialUpdate(id, ssdDTO)
                .map(ssd -> new ResponseEntity<>(ssd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/ssds/{id}")
    public ResponseEntity<SSDDTO> deleteSSD(@NotBlank @PathVariable String id) {
        ssdService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}