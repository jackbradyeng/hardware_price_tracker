package com.price_tracker.controllers;

import com.price_tracker.domain.dto.CPUDTO;
import com.price_tracker.domain.entities.CPUEntity;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.services.CPUService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class CPUController {

    private final CPUService cpuService;
    private final Mapper<CPUEntity, CPUDTO> cpuMapper;

    // cpu create endpoint
    @PostMapping(name = "/cpus")
    public ResponseEntity<CPUDTO> createCPU(@RequestBody final CPUDTO cpuDTO) {
        log.info("Got CPU: {}" + cpuDTO.toString());
        CPUEntity cpuEntity = cpuMapper.mapFrom(cpuDTO);
        CPUEntity savedCPU = cpuService.save(cpuEntity);
        return new ResponseEntity<>(cpuMapper.mapTo(savedCPU), HttpStatus.CREATED);
    }

    // cpu read-all endpoint

    // cpu get-one endpoint

    // cpu update endpoint

    // cpu delete endpoint

}
