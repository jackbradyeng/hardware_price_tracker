package com.price_tracker.controllers.vendor_controllers;

import com.price_tracker.domain.dto.vendor_dtos.VendorDTO;
import com.price_tracker.domain.entities.vendor_entities.VendorEntity;
import com.price_tracker.mappers.Mapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.services.vendor_services.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Log
@RestController
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;
    private final Mapper<VendorEntity, VendorDTO> vendorMapper;

    @Autowired
    public VendorController(VendorService vendorService, MapperFactory mapperFactory) {
        this.vendorService = vendorService;
        this.vendorMapper = mapperFactory.create(VendorEntity.class, VendorDTO.class);
    }

    @PostMapping(path = "/api/vendors")
    public ResponseEntity<VendorDTO> createVendor(@RequestBody final VendorDTO vendorDTO) {
        log.info("Got vendor: {}" + vendorDTO.toString());
        VendorEntity vendorEntity = vendorMapper.mapFrom(vendorDTO);
        VendorEntity savedVendor = vendorService.save(vendorEntity);
        return new ResponseEntity<>(vendorMapper.mapTo(savedVendor), HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/vendors")
    public List<VendorDTO> listVendors() {
        List<VendorEntity> vendors = vendorService.findAll();
        return vendors.stream().map(vendorMapper::mapTo).toList();
    }

    @GetMapping(path = "/api/vendors/{id}")
    public ResponseEntity<VendorDTO> getVendor(@PathVariable String id) {
        Optional<VendorEntity> foundVendor = vendorService.findOne(id);
        return foundVendor.map(vendor -> {
            VendorDTO vendorDTO = vendorMapper.mapTo(vendor);
            return new ResponseEntity<>(vendorDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/vendors/{id}")
    public ResponseEntity<VendorDTO> fullUpdateVendor(@PathVariable String id, @RequestBody VendorDTO vendorDTO) {
        if (!vendorService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vendorDTO.setVendor(id);
        VendorEntity vendorEntity = vendorMapper.mapFrom(vendorDTO);
        VendorEntity savedVendor = vendorService.save(vendorEntity);
        return new ResponseEntity<>(vendorMapper.mapTo(savedVendor), HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/vendors/{id}")
    public ResponseEntity<VendorDTO> deleteVendor(@PathVariable String id) {
        vendorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
