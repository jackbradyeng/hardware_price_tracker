package com.price_tracker.controllers.vendor_controllers;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.services.vendor_services.impl.ScorptecProductServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Log
@Validated
@RestController
@RequiredArgsConstructor
public class ScorptecProductController {

    private final ScorptecProductServiceImpl scorptecProductService;

    @PostMapping(path = "/api/v1/scorptecproducts")
    public ResponseEntity<VendorProductDTO> createProduct(@Valid @RequestBody final VendorProductDTO vendorProductDTO) {
        log.info("Got Scorptec product " + vendorProductDTO.toString());
        VendorProductDTO savedScorptecProduct = scorptecProductService.save(vendorProductDTO);
        return new ResponseEntity<>(savedScorptecProduct, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/v1/scorptecproducts/saveall")
    public ResponseEntity<List<VendorProductDTO>> createProducts(@Valid @RequestBody final List<VendorProductDTO> vendorProductDTOS) {
        log.info("Processing batch of " + vendorProductDTOS.size() + " Scorptec product records");
        List<VendorProductDTO> savedEntities = scorptecProductService.saveAll(vendorProductDTOS);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/scorptecproducts")
    public ResponseEntity<List<VendorProductDTO>> listScorptecProducts() {
        return new ResponseEntity<>(scorptecProductService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> getProduct(@NotBlank @PathVariable String id) {
        Optional<VendorProductDTO> foundProduct = scorptecProductService.findOne(id);
        return foundProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/v1/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> fullUpdateProduct(@NotBlank @PathVariable String id,
                                                              @Valid @RequestBody VendorProductDTO vendorProductDTO) {
        return scorptecProductService.fullUpdate(id, vendorProductDTO)
                .map(updatedScorptecProduct -> new ResponseEntity<>(updatedScorptecProduct, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/v1/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> partialUpdate(@NotBlank @PathVariable String id,
                                                          @Valid @RequestBody VendorProductDTO vendorProductDTO) {
        return scorptecProductService.partialUpdate(id, vendorProductDTO)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/v1/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> deleteProduct(@NotBlank @PathVariable String id) {
        scorptecProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}