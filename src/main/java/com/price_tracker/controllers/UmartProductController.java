package com.price_tracker.controllers;

import com.price_tracker.domain.dto.UmartProductDTO;
import com.price_tracker.domain.entities.UmartProductEntity;
import com.price_tracker.mappers.impl.UmartProductMapper;
import com.price_tracker.services.UmartProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log
public class UmartProductController {

    private final UmartProductService umartProductService;
    private final UmartProductMapper umartProductMapper;

    // umart product create endpoint
    @PostMapping(path = "/umartproducts")
    public ResponseEntity<UmartProductDTO> createProduct(@RequestBody final UmartProductDTO umartProductDTO) {
        log.info("Got product {}" + umartProductDTO.toString());
        UmartProductEntity umartProductEntity = umartProductMapper.mapFrom(umartProductDTO);
        UmartProductEntity savedUmartProduct = umartProductService.save(umartProductEntity);
        return new ResponseEntity<>(umartProductMapper.mapTo(savedUmartProduct), HttpStatus.CREATED);
    }

    // umart product read-all endpoint
    @GetMapping(path = "/umartproducts")
    public List<UmartProductDTO> listUmartProducts() {
        List<UmartProductEntity> umartProducts = umartProductService.findAll();
        return umartProducts.stream().map(umartProductMapper::mapTo).toList();
    }

    // umart product read-one endpoint
    @GetMapping(path = "/umartproducts/{id}")
    public ResponseEntity<UmartProductDTO> getProduct(@PathVariable String id) {
        Optional<UmartProductEntity> foundProduct = umartProductService.findOne(id);
        return foundProduct.map(umartProduct -> {
            UmartProductDTO umartProductDTO = umartProductMapper.mapTo(umartProduct);
            return new ResponseEntity<>(umartProductDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // umart product update endpoint
    @PutMapping(path = "/umartproducts/{id}")
    public ResponseEntity<UmartProductDTO> fullUpdateProduct(@PathVariable String id, @RequestBody UmartProductDTO umartProductDTO) {
        if(!umartProductService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        umartProductDTO.setId(Long.parseLong(id));
        UmartProductEntity umartProductEntity = umartProductMapper.mapFrom(umartProductDTO);
        UmartProductEntity savedUmartProduct = umartProductService.save(umartProductEntity);
        return new ResponseEntity<>(umartProductMapper.mapTo(savedUmartProduct), HttpStatus.OK);
    }

    // umart product delete endpoint
    @DeleteMapping(path = "/umartproducts/{id}")
    public ResponseEntity<UmartProductDTO> deleteProduct(@PathVariable String id) {
        umartProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
