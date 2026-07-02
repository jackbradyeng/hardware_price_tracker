package com.price_tracker.controllers.vendor_controllers;

import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.domain.entities.vendor_entities.ScorptecProductEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.services.vendor_services.ScorptecProductService;
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
public class ScorptecProductController {

    private final ScorptecProductService scorptecProductService;
    private final GenericMapper<ScorptecProductEntity, VendorProductDTO> scorptecProductMapper;

    @Autowired
    public ScorptecProductController(ScorptecProductService scorptecProductService, MapperFactory mapperFactory) {
        this.scorptecProductService = scorptecProductService;
        this.scorptecProductMapper = mapperFactory.create(ScorptecProductEntity.class, VendorProductDTO.class);
    }

    @PostMapping(path = "/api/scorptecproducts")
    public ResponseEntity<VendorProductDTO> createProduct(@RequestBody final VendorProductDTO vendorProductDTO) {
        log.info("Got product {}" + vendorProductDTO.toString());
        ScorptecProductEntity scorptecProductEntity = scorptecProductMapper.mapFrom(vendorProductDTO);
        ScorptecProductEntity savedScorptecProduct = scorptecProductService.save(scorptecProductEntity);
        return new ResponseEntity<>(scorptecProductMapper.mapTo(savedScorptecProduct), HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/scorptecproducts/saveall")
    public ResponseEntity<List<VendorProductDTO>> createProducts(@RequestBody final List<VendorProductDTO> vendorProductDTOS) {
        ArrayList<VendorProductDTO> responseList = new ArrayList<>();
        for (VendorProductDTO vendorProductDTO : vendorProductDTOS) {
            log.info("Got Scorptec Product: {}" + vendorProductDTO.toString());
            ScorptecProductEntity scorptecProductEntity = scorptecProductMapper.mapFrom(vendorProductDTO);
            ScorptecProductEntity savedScorptecProduct = scorptecProductService.save(scorptecProductEntity);
            responseList.add(scorptecProductMapper.mapTo(savedScorptecProduct));
        }
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/scorptecproducts")
    public List<VendorProductDTO> listScorptecProducts() {
        List<ScorptecProductEntity> scorptecProducts = scorptecProductService.findAll();
        return scorptecProducts.stream().map(scorptecProductMapper::mapTo).toList();
    }

    @GetMapping(path = "/api/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> getProduct(@PathVariable String id) {
        Optional<ScorptecProductEntity> foundProduct = scorptecProductService.findOne(id);
        return foundProduct.map(scorptecProduct -> {
            VendorProductDTO vendorProductDTO = scorptecProductMapper.mapTo(scorptecProduct);
            return new ResponseEntity<>(vendorProductDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> fullUpdateProduct(@PathVariable String id, @RequestBody VendorProductDTO vendorProductDTO) {
        if(!scorptecProductService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vendorProductDTO.setId(Long.parseLong(id));
        ScorptecProductEntity scorptecProductEntity = scorptecProductMapper.mapFrom(vendorProductDTO);
        ScorptecProductEntity savedScorptecProduct = scorptecProductService.save(scorptecProductEntity);
        return new ResponseEntity<>(scorptecProductMapper.mapTo(savedScorptecProduct), HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/scorptecproducts/{id}")
    public ResponseEntity<VendorProductDTO> deleteProduct(@PathVariable String id) {
        scorptecProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}