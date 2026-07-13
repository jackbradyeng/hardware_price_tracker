package com.price_tracker.controllers.vendor_controllers;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.services.vendor_services.impl.ScorptecProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@RestController
@RequiredArgsConstructor
public class ScorptecProductController {

    private final ScorptecProductServiceImpl scorptecProductService;

    @PostMapping(path = "/api/scorptecproducts")
    public ResponseEntity<VendorProductDTO> createProduct(@RequestBody final VendorProductDTO vendorProductDTO) {
        log.info("Got Scorptec product " + vendorProductDTO.toString());
        VendorProductDTO savedScorptecProduct = scorptecProductService.save(vendorProductDTO);
        return new ResponseEntity<>(savedScorptecProduct, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/scorptecproducts/saveall")
    public ResponseEntity<List<VendorProductDTO>> createProducts(@RequestBody final List<VendorProductDTO> vendorProductDTOS) {
        ArrayList<VendorProductDTO> responseList = new ArrayList<>();
        for (VendorProductDTO vendorProductDTO : vendorProductDTOS) {
            log.info("Got Scorptec Product: " + vendorProductDTO.toString());
            VendorProductDTO savedScorptecProduct = scorptecProductService.save(vendorProductDTO);
            responseList.add(savedScorptecProduct);
        }
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/scorptecproducts")
    public List<VendorProductDTO> listScorptecProducts() {
        return scorptecProductService.findAll();
    }

    @GetMapping(path = "/api/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> getProduct(@PathVariable String id) {
        Optional<VendorProductDTO> foundProduct = scorptecProductService.findOne(id);
        return foundProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> fullUpdateProduct(@PathVariable String id,
                                                              @RequestBody VendorProductDTO vendorProductDTO) {
        return scorptecProductService.fullUpdate(id, vendorProductDTO)
                .map(updatedScorptecProduct -> new ResponseEntity<>(updatedScorptecProduct, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> deleteProduct(@PathVariable String id) {
        scorptecProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}