package com.price_tracker.controllers;

import com.price_tracker.domain.dto.RAMDTO;
import com.price_tracker.domain.entities.RAM;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.mappers.impl.RAMMapper;
import com.price_tracker.services.RAMService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/** Fully functional REST API with CRUD functionality. */
@RestController
@Log
public class RAMController {

    private final RAMService ramService;
    private final Mapper<RAM, RAMDTO> ramMapper;

    // ram service dependency injection
    public RAMController(RAMService ramService, RAMMapper ramMapper) {
        this.ramService = ramService;
        this.ramMapper = ramMapper;
    }

    // ram create endpoint
    @PostMapping(path = "/ram")
    public ResponseEntity<RAMDTO> createRAM(@RequestBody final RAMDTO ramDTO) {
        log.info("Got RAM: {}" + ramDTO.toString());
        RAM ram = ramMapper.mapFrom(ramDTO);
        RAM savedRAM = ramService.createRAM(ram);
        return new ResponseEntity<>(ramMapper.mapTo(savedRAM), HttpStatus.CREATED);
    }

    // ram read-all endpoint
    @GetMapping(path = "/ram")
    public List<RAMDTO> listRAM() {
        List<RAM> ram = ramService.findAll();
        return ram.stream()
                .map(ramMapper::mapTo)
                .toList();
    }

    // ram get-one endpoint
    @GetMapping(path = "/ram/{id}")
    public ResponseEntity<RAMDTO> getRAM(@PathVariable("id") String id) {
        Optional<RAM> foundRAM = ramService.findOne(id);
        return foundRAM.map(ram -> {
            RAMDTO ramdto = ramMapper.mapTo(ram);
            return new ResponseEntity<>(ramdto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
