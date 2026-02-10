package com.price_tracker.controllers;

import com.price_tracker.domain.dto.RAMDTO;
import com.price_tracker.domain.entities.RAM;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.mappers.impl.RAMMapper;
import com.price_tracker.services.RAMService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

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

    // ram read endpoint
    @GetMapping(path = "/ram")
    public RAM retrieveRAM() {
        return RAM.builder()
                .id(" KF556C36BBEK2/32")
                .name("Kingston 32GB (2x16GB) KF556C36BBEK2/32 Fury Beast CL36 5600MHz DDR5 RAM Black")
                .brand("Kingston")
                .volume(32)
                .clockRate(6000)
                .price(new BigDecimal("649.00"))
                .build();
    }
}
