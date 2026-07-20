package com.priceTracker.controllers.pricePointControllers;

import com.priceTracker.domain.dto.hybridDTOs.NVMEDataAndPricePointDTO;
import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.services.pricePointServices.GenericPricePointService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
public class NVMEPricePointController {

    private final GenericPricePointService<NVMEDataAndPricePointDTO> nvmePricePointService;

    @GetMapping(path = "/api/v1/nvme_pricepoints")
    public ResponseEntity<Page<GenericPricePointDTO>> listNVMEPricePoints(
            @PageableDefault(size = 30) Pageable pageable) {
        return new ResponseEntity<>(nvmePricePointService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/nvme_pricepoints/{modelNumber}")
    public ResponseEntity<NVMEDataAndPricePointDTO> findNVMEPricePointsByModelNumber(
            @NotBlank @PathVariable String modelNumber,
            @PageableDefault(size = 30) Pageable pageable) {

        Optional<NVMEDataAndPricePointDTO> nvmePricePointDTOS = nvmePricePointService
                .findByModelNumber(modelNumber, pageable);

        return nvmePricePointDTOS.map(foundPriceHistory ->
                new ResponseEntity<>(foundPriceHistory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}