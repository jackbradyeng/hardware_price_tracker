package com.price_tracker.controllers.vendor_controllers;

import com.price_tracker.domain.dto.vendor_dtos.VendorDTO;
import com.price_tracker.services.vendor_services.impl.VendorServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Log
@RestController
@RequiredArgsConstructor
public class VendorController {

    private final VendorServiceImpl vendorService;

    @PostMapping(path = "/api/vendors")
    public ResponseEntity<VendorDTO> createVendor(@Valid @RequestBody final VendorDTO vendorDTO) {
        log.info("Got vendor: " + vendorDTO.toString());
        VendorDTO savedVendor = vendorService.save(vendorDTO);
        return new ResponseEntity<>(savedVendor, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/vendors/saveall")
    public ResponseEntity<List<VendorDTO>> createVendors(@Valid @RequestBody final List<VendorDTO> vendorDTOS) {
        log.info("Processing batch of " + vendorDTOS.size() + " vendor records.");
        List<VendorDTO> savedVendors = vendorService.saveAll(vendorDTOS);
        return new ResponseEntity<>(savedVendors, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/vendors")
    public ResponseEntity<List<VendorDTO>> listVendors() {
        return new ResponseEntity<>(vendorService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/vendors/{id}")
    public ResponseEntity<VendorDTO> getVendor(@NotBlank @PathVariable String id) {
        Optional<VendorDTO> foundVendor = vendorService.findOne(id);
        return foundVendor.map(vendor -> new ResponseEntity<>(vendor, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/vendors/{id}")
    public ResponseEntity<VendorDTO> fullUpdateVendor(@NotBlank @PathVariable String id,
                                                       @Valid @RequestBody VendorDTO vendorDTO) {
        return vendorService.fullUpdate(id, vendorDTO)
                .map(updatedVendor -> new ResponseEntity<>(updatedVendor, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/vendors/{id}")
    public ResponseEntity<VendorDTO> partialUpdateVendor(@NotBlank @PathVariable String id,
                                                          @Valid @RequestBody VendorDTO vendorDTO) {
        return vendorService.partialUpdate(id, vendorDTO)
                .map(vendor -> new ResponseEntity<>(vendor, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/vendors/{id}")
    public ResponseEntity<VendorDTO> deleteVendor(@NotBlank @PathVariable String id) {
        vendorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}