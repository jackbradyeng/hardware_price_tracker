package com.priceTracker.controllers.vendorControllers;

import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.services.vendorServices.impl.UmartProductServiceImpl;
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
public class UmartProductController {

    private final UmartProductServiceImpl umartProductService;

    // CREATE ENDPOINTS (ADMIN ONLY)
    @PostMapping(path = "/api/v1/umartproducts")
    public ResponseEntity<VendorProductDTO> createProduct(@Valid @RequestBody final VendorProductDTO vendorProductDTO) {
        log.info("Got Umart product " + vendorProductDTO.toString());
        VendorProductDTO savedUmartProduct = umartProductService.save(vendorProductDTO);
        return new ResponseEntity<>(savedUmartProduct, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/v1/umartproducts/saveall")
    public ResponseEntity<List<VendorProductDTO>> createProducts(@Valid @RequestBody final List<VendorProductDTO> vendorProductDTOS) {
        log.info("Processing batch of " + vendorProductDTOS.size() + " Umart product records.");
        List<VendorProductDTO> savedEntities = umartProductService.saveAll(vendorProductDTOS);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    // GET ENDPOINTS (PUBLIC)
    @GetMapping(path = "/api/v1/umartproducts")
    public ResponseEntity<List<VendorProductDTO>> listUmartProducts() {
        return new ResponseEntity<>(umartProductService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> getProduct(@NotBlank @PathVariable String id) {
        Optional<VendorProductDTO> foundProduct = umartProductService.findOne(id);
        return foundProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET ENDPOINTS (PRODUCT URLS) - TO BE CONSUMED BY THE SCRAPING MICROSERVICE
    @GetMapping(path = "/api/v1/umartproducts/gpu-page-links")
    public ResponseEntity<List<String>> getUmartGPULinks() {
        return new ResponseEntity<>(umartProductService.findUmartGPULinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/umartproducts/ram-page-links")
    public ResponseEntity<List<String>> getUmartRAMLinks() {
        return new ResponseEntity<>(umartProductService.findUmartRAMLinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/umartproducts/cpu-page-links")
    public ResponseEntity<List<String>> getUmartCPULinks() {
        return new ResponseEntity<>(umartProductService.findUmartCPULinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/umartproducts/workstation-gpu-page-links")
    public ResponseEntity<List<String>> getUmartWorkstationGPULinks() {
        return new ResponseEntity<>(umartProductService.findUmartWorkstationGPULinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/umartproducts/hdd-page-links")
    public ResponseEntity<List<String>> getUmartHDDLinks() {
        return new ResponseEntity<>(umartProductService.findUmartHDDLinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/umartproducts/ssd-page-links")
    public ResponseEntity<List<String>> getUmartSSDLinks() {
        return new ResponseEntity<>(umartProductService.findUmartSSDLinks(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/umartproducts/nvme-page-links")
    public ResponseEntity<List<String>> getUmartNVMELinks() {
        return new ResponseEntity<>(umartProductService.findUmartNVMELinks(), HttpStatus.OK);
    }

    // UPDATE ENDPOINTS (ADMIN ONLY)
    @PutMapping(path = "/api/v1/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> fullUpdateProduct(@NotBlank @PathVariable String id,
                                                              @Valid @RequestBody VendorProductDTO vendorProductDTO) {
        return umartProductService.fullUpdate(id, vendorProductDTO)
                .map(updatedUmartProduct -> new ResponseEntity<>(updatedUmartProduct, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/v1/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> partialUpdateProduct(@NotBlank @PathVariable String id,
                                                                 @Valid @RequestBody VendorProductDTO vendorProductDTO) {
        return umartProductService.partialUpdate(id, vendorProductDTO)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE ENDPOINT (ADMIN ONLY)
    @DeleteMapping(path = "/api/v1/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> deleteProduct(@NotBlank @PathVariable String id) {
        umartProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}