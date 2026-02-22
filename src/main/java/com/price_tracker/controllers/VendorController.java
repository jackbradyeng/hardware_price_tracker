package com.price_tracker.controllers;

import com.price_tracker.domain.dto.VendorDTO;
import com.price_tracker.domain.entities.VendorEntity;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.services.VendorService;
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
public class VendorController {

    private final VendorService vendorService;
    private final Mapper<VendorEntity, VendorDTO> vendorMapper;

    // vendor create endpoint
    @PostMapping(path = "/vendors")
    public ResponseEntity<VendorDTO> createVendor(@RequestBody final VendorDTO vendorDTO) {
        log.info("Got vendor: {}" + vendorDTO.toString());
        VendorEntity vendorEntity = vendorMapper.mapFrom(vendorDTO);
        VendorEntity savedVendor = vendorService.save(vendorEntity);
        return new ResponseEntity<>(vendorMapper.mapTo(savedVendor), HttpStatus.CREATED);
    }

    // vendor read-all endpoint
    @GetMapping(path = "/vendors")
    public List<VendorDTO> listVendors() {
        List<VendorEntity> vendors = vendorService.findAll();
        return vendors.stream().map(vendorMapper::mapTo).toList();
    }

    // vendor read-one endpoint
    @GetMapping(path = "/vendors/{id}")
    public ResponseEntity<VendorDTO> getVendor(@PathVariable String id) {
        Optional<VendorEntity> foundVendor = vendorService.findOne(id);
        return foundVendor.map(vendor -> {
            VendorDTO vendorDTO = vendorMapper.mapTo(vendor);
            return new ResponseEntity<>(vendorDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //vendor update endpoint
    @PutMapping(path = "/vendors/{id}")
    public ResponseEntity<VendorDTO> fullUpdateVendor(@PathVariable String id, @RequestBody VendorDTO vendorDTO) {
        if (!vendorService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vendorDTO.setVendor(id);
        VendorEntity vendorEntity = vendorMapper.mapFrom(vendorDTO);
        VendorEntity savedVendor = vendorService.save(vendorEntity);
        return new ResponseEntity<>(vendorMapper.mapTo(savedVendor), HttpStatus.OK);
    }

    //vendor delete endpoint
    @DeleteMapping(path = "/vendors/{id}")
    public ResponseEntity<VendorDTO> deleteVendor(@PathVariable String id) {
        vendorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
