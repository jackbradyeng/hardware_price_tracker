package com.price_tracker.controllers.price_point_controllers;

import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.RAMPricePointDTO;
import com.price_tracker.services.price_point_services.RAMPricePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log
public class RAMPricePointController {

    private final RAMPricePointService ramPricePointService;

    // ram price point read-all endpoint
    @GetMapping(path = "/api/ram_pricepoints")
    public ResponseEntity<List<RAMPricePointDTO>> listRAMPricePoints() {
        return new ResponseEntity<>(ramPricePointService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/ram_pricepoints/{modelNumber}")
    public ResponseEntity<RAMDataAndPricePointDTO> findRAMPricePointsBYModelNumber(
            @PathVariable String modelNumber) {
        return new ResponseEntity<>(ramPricePointService.findByModelNumber(modelNumber), HttpStatus.OK);
    }
}
