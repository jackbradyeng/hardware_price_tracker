package com.price_tracker.repositories;

import com.price_tracker.domain.entities.GPU;
import com.price_tracker.domain.entities.RAM;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TestDataUtility {

    public GPU createTestGPU() {
        String modelNumber = "PRIME-RTX5070TI-O16G";
        String name = "Asus Prime GeForce RTX 5070 Ti OC 16G Graphics Card";
        String brand = "ASUS";
        BigDecimal price = new BigDecimal("1598.00");

        return GPU.builder()
                .modelNumber(modelNumber)
                .name(name)
                .brand(brand)
                .price(price)
                .build();
    }

    public RAM createTestRAM() {
        String id = "KF560C36BBE2K2-64";
        String name = "Kingston 64GB (2x32GB) Fury Beast CL36 6000MHz DDR5 RAM (KF560C36BBE2K2-64)";
        String brand = "Kingston";
        BigDecimal price = new BigDecimal("1299.00");

        return RAM.builder()
                .id(id)
                .name(name)
                .brand(brand)
                .price(price)
                .build();
    }
}
