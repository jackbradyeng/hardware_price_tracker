package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.services.product_services.GenericProductService;
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
    public ResponseEntity<HDDDTO> createHDD(@RequestBody final HDDDTO hddDTO) {
        log.info("Got HDD: {}" + hddDTO.toString());
        HDDDTO savedHDD = hddService.save(hddDTO);
        return new ResponseEntity<>(savedHDD, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/hdds/saveall")
    public ResponseEntity<List<HDDDTO>> createHDDs(@RequestBody final List<HDDDTO> hddDTOs) {
        log.info("Processing batch of " + hddDTOs.size() + " HDD records");
        List<HDDDTO> savedEntities = hddService.saveAll(hddDTOs);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/hdds")
    public List<HDDDTO> listHDDs() {
        return hddService.findAll();
    }

    @GetMapping(path = "/api/hdds/{id}")
    public ResponseEntity<HDDDTO> getHDD(@PathVariable String id) {
        Optional<HDDDTO> foundHDD = hddService.findOne(id);
        return foundHDD.map(hdd -> new ResponseEntity<>(hdd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/hdds/{id}")
    public ResponseEntity<HDDDTO> fullUpdateHDD(@PathVariable String id, @RequestBody HDDDTO hddDTO) {
        return hddService.fullUpdate(id, hddDTO)
                .map(hdd -> new ResponseEntity<>(hdd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/hdds/{id}")
    public ResponseEntity<HDDDTO> partialUpdate(@PathVariable String id, @RequestBody HDDDTO hddDTO) {
        return hddService.partialUpdate(id, hddDTO)
                .map(hdd -> new ResponseEntity<>(hdd, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/hdds/{id}")
    public ResponseEntity<HDDDTO> deleteHDD(@PathVariable String id) {
        hddService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}