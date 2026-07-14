package com.price_tracker.controllers.vendor_controllers;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.services.vendor_services.impl.UmartProductServiceImpl;
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
public class UmartProductController {

    private final UmartProductServiceImpl umartProductService;

    @PostMapping(path = "/api/umartproducts")
    public ResponseEntity<VendorProductDTO> createProduct(@RequestBody final VendorProductDTO vendorProductDTO) {
        log.info("Got Umart product " + vendorProductDTO.toString());
        VendorProductDTO savedUmartProduct = umartProductService.save(vendorProductDTO);
        return new ResponseEntity<>(savedUmartProduct, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/umartproducts/saveall")
    public ResponseEntity<List<VendorProductDTO>> createProducts(@RequestBody final List<VendorProductDTO> vendorProductDTOS) {
        log.info("Processing batch of " + vendorProductDTOS.size() + " Umart product records.");
        List<VendorProductDTO> savedEntities = umartProductService.saveAll(vendorProductDTOS);
        return new ResponseEntity<>(savedEntities, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/umartproducts")
    public ResponseEntity<List<VendorProductDTO>> listUmartProducts() {
        return new ResponseEntity<>(umartProductService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> getProduct(@PathVariable String id) {
        Optional<VendorProductDTO> foundProduct = umartProductService.findOne(id);
        return foundProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> fullUpdateProduct(@PathVariable String id,
                                                              @RequestBody VendorProductDTO vendorProductDTO) {
        return umartProductService.fullUpdate(id, vendorProductDTO)
                .map(updatedUmartProduct -> new ResponseEntity<>(updatedUmartProduct, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> partialUpdateProduct(@PathVariable String id,
                                                                 @RequestBody VendorProductDTO vendorProductDTO) {
        return umartProductService.partialUpdate(id, vendorProductDTO)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> deleteProduct(@PathVariable String id) {
        umartProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}