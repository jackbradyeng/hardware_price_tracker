package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.RAMDTO;
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
public class RAMController {

    private final GenericProductService<RAMDTO> ramService;

    @PostMapping(path = "/api/ram")
    public ResponseEntity<RAMDTO> createRAM(@Valid @RequestBody final RAMDTO ramDTO) {
        log.info("Got RAM: " + ramDTO.toString());
        RAMDTO savedRAM = ramService.save(ramDTO);
        return new ResponseEntity<>(savedRAM, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/ram/saveall")
    public ResponseEntity<List<RAMDTO>> createRam(@Valid @RequestBody final List<RAMDTO> ramDTOs) {
        log.info("Processing batch of " + ramDTOs.size() + " RAM records");
        List<RAMDTO> savedEntities = ramService.saveAll(ramDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/ram")
    public ResponseEntity<List<RAMDTO>> listRAM() {
        return new ResponseEntity<>(ramService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/ram/{id}")
    public ResponseEntity<RAMDTO> getRAM(@NotBlank @PathVariable String id) {
        Optional<RAMDTO> foundRAM = ramService.findOne(id);
        return foundRAM.map(ram -> new ResponseEntity<>(ram, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/ram/{id}")
    public ResponseEntity<RAMDTO> fullUpdateRAM(@NotBlank @PathVariable String id, @Valid @RequestBody RAMDTO ramDTO) {
        return ramService.fullUpdate(id, ramDTO)
                .map(ram -> new ResponseEntity<>(ram, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/ram/{id}")
    public ResponseEntity<RAMDTO> partialUpdate(@NotBlank @PathVariable String id, @Valid @RequestBody RAMDTO ramDTO) {
        return ramService.partialUpdate(id, ramDTO)
                .map(ram -> new ResponseEntity<>(ram, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/ram/{id}")
    public ResponseEntity<RAMDTO> deleteRAM(@NotBlank @PathVariable String id) {
        ramService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}