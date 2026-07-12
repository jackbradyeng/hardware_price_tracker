package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.HDDDTO;
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

@RestController
@RequiredArgsConstructor
@Log
public class HDDController {

    private final GenericProductService<HDDDTO> hddService;

    @PostMapping(path = "/api/hdds")
    public ResponseEntity<HDDDTO> createHDD(@Valid @RequestBody final HDDDTO hddDTO) {
        log.info("Got HDD: " + hddDTO.toString());
        HDDDTO savedHDD = hddService.save(hddDTO);
        return new ResponseEntity<>(savedHDD, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/hdds/saveall")
    public ResponseEntity<List<HDDDTO>> createHDDs(@Valid @RequestBody final List<HDDDTO> hddDTOs) {
        log.info("Processing batch of " + hddDTOs.size() + " HDD records");
        List<HDDDTO> savedEntities = hddService.saveAll(hddDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/hdds")
    public ResponseEntity<List<HDDDTO>> listHDDs() {
        return new ResponseEntity<>(hddService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/hdds/{id}")
    public ResponseEntity<HDDDTO> getHDD(@NotBlank @PathVariable String id) {
        Optional<HDDDTO> foundHDD = hddService.findOne(id);
        return foundHDD.map(hdd -> new ResponseEntity<>(hdd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/hdds/{id}")
    public ResponseEntity<HDDDTO> fullUpdateHDD(@NotBlank @PathVariable String id, @Valid @RequestBody HDDDTO hddDTO) {
        return hddService.fullUpdate(id, hddDTO)
                .map(hdd -> new ResponseEntity<>(hdd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/hdds/{id}")
    public ResponseEntity<HDDDTO> partialUpdate(@NotBlank @PathVariable String id, @Valid @RequestBody HDDDTO hddDTO) {
        return hddService.partialUpdate(id, hddDTO)
                .map(hdd -> new ResponseEntity<>(hdd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/hdds/{id}")
    public ResponseEntity<HDDDTO> deleteHDD(@NotBlank @PathVariable String id) {
        hddService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}