package com.price_tracker.controllers.vendor_controllers;

import com.price_tracker.domain.dto.vendor_dtos.UmartProductDTO;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.mappers.vendor_mappers.UmartProductMapper;
import com.price_tracker.services.vendor_services.UmartProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log
public class UmartProductController {

    private final UmartProductService umartProductService;
    private final UmartProductMapper umartProductMapper;

    @PostMapping(path = "/api/umartproducts")
    public ResponseEntity<UmartProductDTO> createProduct(@RequestBody final UmartProductDTO umartProductDTO) {
        log.info("Got product {}" + umartProductDTO.toString());
        UmartProductEntity umartProductEntity = umartProductMapper.mapFrom(umartProductDTO);
        UmartProductEntity savedUmartProduct = umartProductService.save(umartProductEntity);
        return new ResponseEntity<>(umartProductMapper.mapTo(savedUmartProduct), HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/umartproducts/saveall")
    public ResponseEntity<List<UmartProductDTO>> createProducts(@RequestBody final List<UmartProductDTO> umartProductDTOs) {
        ArrayList<UmartProductDTO> responseList = new ArrayList<>();
        for (UmartProductDTO umartProductDTO : umartProductDTOs) {
            log.info("Got Umart Product: {}" + umartProductDTO.toString());
            UmartProductEntity umartProductEntity = umartProductMapper.mapFrom(umartProductDTO);
            UmartProductEntity savedUmartProduct = umartProductService.save(umartProductEntity);
            responseList.add(umartProductMapper.mapTo(savedUmartProduct));
        }
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/umartproducts")
    public List<UmartProductDTO> listUmartProducts() {
        List<UmartProductEntity> umartProducts = umartProductService.findAll();
        return umartProducts.stream().map(umartProductMapper::mapTo).toList();
    }

    @GetMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<UmartProductDTO> getProduct(@PathVariable String id) {
        Optional<UmartProductEntity> foundProduct = umartProductService.findOne(id);
        return foundProduct.map(umartProduct -> {
            UmartProductDTO umartProductDTO = umartProductMapper.mapTo(umartProduct);
            return new ResponseEntity<>(umartProductDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<UmartProductDTO> fullUpdateProduct(@PathVariable String id, @RequestBody UmartProductDTO umartProductDTO) {
        if(!umartProductService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        umartProductDTO.setId(Long.parseLong(id));
        UmartProductEntity umartProductEntity = umartProductMapper.mapFrom(umartProductDTO);
        UmartProductEntity savedUmartProduct = umartProductService.save(umartProductEntity);
        return new ResponseEntity<>(umartProductMapper.mapTo(savedUmartProduct), HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<UmartProductDTO> deleteProduct(@PathVariable String id) {
        umartProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
