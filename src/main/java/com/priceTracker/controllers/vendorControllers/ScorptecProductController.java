package com.priceTracker.controllers.vendorControllers;

import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.services.vendorServices.impl.ScorptecProductServiceImpl;
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

    // CREATE ENDPOINTS (ADMIN ONLY)
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

    // GET ENDPOINTS (PUBLIC)
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

    // GET ENDPOINTS (PRODUCT URLS) - TO BE CONSUMED BY THE SCRAPING MICROSERVICE
    @GetMapping(path = "/api/v1/scorptecproducts/gpu-page-links")
    public ResponseEntity<List<String>> getScorptecGPULinks() {
        return new ResponseEntity<>(scorptecProductService.findScorptecGPULinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/scorptecproducts/ram-page-links")
    public ResponseEntity<List<String>> getScorptecRAMLinks() {
        return new ResponseEntity<>(scorptecProductService.findScorptecRAMLinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/scorptecproducts/cpu-page-links")
    public ResponseEntity<List<String>> getScorptecCPULinks() {
        return new ResponseEntity<>(scorptecProductService.findScorptecCPULinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/scorptecproducts/workstation-gpu-page-links")
    public ResponseEntity<List<String>> getScorptecWorkstationGPULinks() {
        return new ResponseEntity<>(scorptecProductService.findScorptecWorkstationGPULinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/scorptecproducts/hdd-page-links")
    public ResponseEntity<List<String>> getScorptecHDDLinks() {
        return new ResponseEntity<>(scorptecProductService.findScorptecHDDLinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/scorptecproducts/ssd-page-links")
    public ResponseEntity<List<String>> getScorptecSSDLinks() {
        return new ResponseEntity<>(scorptecProductService.findScorptecSSDLinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/scorptecproducts/nvme-page-links")
    public ResponseEntity<List<String>> getScorptecNVMELinks() {
        return new ResponseEntity<>(scorptecProductService.findScorptecNVMELinks(), HttpStatus.OK);
    }

    // UPDATE ENDPOINTS (ADMIN ONLY)
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

    // DELETE ENDPOINT (ADMIN ONLY)
    @DeleteMapping(path = "/api/v1/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> deleteProduct(@NotBlank @PathVariable String id) {
        scorptecProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}