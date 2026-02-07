package com.price_tracker;

import com.price_tracker.domain.GPU;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Log
public class PriceTrackerController {

    // hello world end point
    @GetMapping(path = "/hello")
    public String helloWorld() {
        return "Hello World.";
    }

    // gpu end point
    @GetMapping(path = "/gpus")
    public GPU retrieveGPU(@RequestBody final GPU gpu) {
        return GPU.builder()
                .modelNumber("PRIME-RTX5070TI-O16G")
                .name("Asus Prime GeForce RTX 5070 Ti OC 16G Graphics Card")
                .chipManufacturer("NVIDIA")
                .boardManufacturer("ASUS")
                .videoMemory(16)
                .price(new BigDecimal("1598.00"))
                .build();
    }

    @PostMapping(path = "/gpus")
    public GPU createGPU(@RequestBody final GPU gpu) {
        log.info("Got GPU: {}" + gpu.toString());
        return gpu;
    }
}

