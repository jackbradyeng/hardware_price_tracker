package com.price_tracker.controllers;

import com.price_tracker.domain.dto.RAMDTO;
import com.price_tracker.domain.entities.RAMEntity;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.services.RAMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/** Fully functional REST API with CRUD functionality. */
@RestController
@RequiredArgsConstructor
@Log
public class RAMController {

    private final RAMService ramService;
    private final Mapper<RAMEntity, RAMDTO> ramMapper;

    // ram create endpoint
    @PostMapping(path = "/ram")
    public ResponseEntity<RAMDTO> createRAM(@RequestBody final RAMDTO ramDTO) {
        log.info("Got RAM: {}" + ramDTO.toString());
        RAMEntity ramEntity = ramMapper.mapFrom(ramDTO);
        RAMEntity savedRAMEntity = ramService.save(ramEntity);
        return new ResponseEntity<>(ramMapper.mapTo(savedRAMEntity), HttpStatus.CREATED);
    }

    // ram read-all endpoint
    @GetMapping(path = "/ram")
    public List<RAMDTO> listRAM() {
        List<RAMEntity> ramEntity = ramService.findAll();
        return ramEntity.stream()
                .map(ramMapper::mapTo)
                .toList();
    }

    // ram get-one endpoint
    @GetMapping(path = "/ram/{id}")
    public ResponseEntity<RAMDTO> getRAM(@PathVariable("id") String id) {
        Optional<RAMEntity> foundRAM = ramService.findOne(id);
        return foundRAM.map(ram -> {
            RAMDTO ramdto = ramMapper.mapTo(ram);
            return new ResponseEntity<>(ramdto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ram update endpoint
    @PutMapping(path = "/ram/{id}")
    public ResponseEntity<RAMDTO> fullUpdateRAM(
            @PathVariable("id") String id,
            @RequestBody RAMDTO ramDTO) {
        if(ramService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ramDTO.setModelNumber(id);
        RAMEntity ramEntity = ramMapper.mapFrom(ramDTO);
        RAMEntity savedRAMEntity = ramService.save(ramEntity);
        return new ResponseEntity<>(
                ramMapper.mapTo(savedRAMEntity),
                HttpStatus.OK
        );
    }

    // ram partial-update endpoint
    @PatchMapping(path = "/ram/{id}")
    public ResponseEntity<RAMDTO> partialUpdate(
            @PathVariable String id,
            @RequestBody RAMDTO ramDTO) {
        if(!ramService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        RAMEntity ramEntity = ramMapper.mapFrom(ramDTO);
        RAMEntity updatedRAMEntity = ramService.partialUpdate(id, ramEntity);
        return new ResponseEntity<>(
                ramMapper.mapTo(updatedRAMEntity),
                HttpStatus.OK
        );
    }

    // ram delete endpoint
    @DeleteMapping(path = "/ram/{id}")
    public ResponseEntity<RAMDTO> deleteRAM(@PathVariable("id") String id) {
        ramService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
