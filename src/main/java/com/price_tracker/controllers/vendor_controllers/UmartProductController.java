package com.price_tracker.controllers.vendor_controllers;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.services.vendor_services.UmartProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@RestController
@RequiredArgsConstructor
public class UmartProductController {

    private final UmartProductService umartProductService;
    private final GenericMapper<UmartProductEntity, VendorProductDTO> umartProductMapper;

    @Autowired
    public UmartProductController(UmartProductService umartProductService, MapperFactory mapperFactory) {
        this.umartProductService = umartProductService;
        this.umartProductMapper = mapperFactory.create(UmartProductEntity.class, VendorProductDTO.class);
    }

    @PostMapping(path = "/api/umartproducts")
    public ResponseEntity<VendorProductDTO> createProduct(@RequestBody final VendorProductDTO vendorProductDTO) {
        log.info("Got product {}" + vendorProductDTO.toString());
        UmartProductEntity umartProductEntity = umartProductMapper.mapFrom(vendorProductDTO);
        UmartProductEntity savedUmartProduct = umartProductService.save(umartProductEntity);
        return new ResponseEntity<>(umartProductMapper.mapTo(savedUmartProduct), HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/umartproducts/saveall")
    public ResponseEntity<List<VendorProductDTO>> createProducts(@RequestBody final List<VendorProductDTO> vendorProductDTOS) {
        ArrayList<VendorProductDTO> responseList = new ArrayList<>();
        for (VendorProductDTO vendorProductDTO : vendorProductDTOS) {
            log.info("Got Umart Product: {}" + vendorProductDTO.toString());
            UmartProductEntity umartProductEntity = umartProductMapper.mapFrom(vendorProductDTO);
            UmartProductEntity savedUmartProduct = umartProductService.save(umartProductEntity);
            responseList.add(umartProductMapper.mapTo(savedUmartProduct));
        }
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/umartproducts")
    public List<VendorProductDTO> listUmartProducts() {
        List<UmartProductEntity> umartProducts = umartProductService.findAll();
        return umartProducts.stream().map(umartProductMapper::mapTo).toList();
    }

    @GetMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> getProduct(@PathVariable String id) {
        Optional<UmartProductEntity> foundProduct = umartProductService.findOne(id);
        return foundProduct.map(umartProduct -> {
            VendorProductDTO vendorProductDTO = umartProductMapper.mapTo(umartProduct);
            return new ResponseEntity<>(vendorProductDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> fullUpdateProduct(@PathVariable String id, @RequestBody VendorProductDTO vendorProductDTO) {
        if(!umartProductService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vendorProductDTO.setId(Long.parseLong(id));
        UmartProductEntity umartProductEntity = umartProductMapper.mapFrom(vendorProductDTO);
        UmartProductEntity savedUmartProduct = umartProductService.save(umartProductEntity);
        return new ResponseEntity<>(umartProductMapper.mapTo(savedUmartProduct), HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/umartproducts/{id}")
    public ResponseEntity<VendorProductDTO> deleteProduct(@PathVariable String id) {
        umartProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
