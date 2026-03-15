package com.price_tracker.controllers;

import com.price_tracker.domain.dto.RAMPricePointDTO;
import com.price_tracker.services.price_points.RAMPricePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log
public class RAMPricePointController {

    private final RAMPricePointService ramPricePointService;

    // ram price point read-all endpoint
    @GetMapping(path = "/ram_pricepoints")
    public ResponseEntity<List<RAMPricePointDTO>> listRAMPricePoints() {
        return new ResponseEntity<>(ramPricePointService.findAll(), HttpStatus.OK);
    }

    // ram price point read-one endpoint
    @GetMapping(path = "/ram_pricepoints/{id}")
    public ResponseEntity<RAMPricePointDTO> getRAMPricePoint(@PathVariable Long id) {

        Optional<RAMPricePointDTO> foundRAM = ramPricePointService.findOne(id);

        return foundRAM.map(ram ->
                new ResponseEntity<>(ram, HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
