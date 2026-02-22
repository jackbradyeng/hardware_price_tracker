package com.price_tracker.repositories;

import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.domain.entities.RAMEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestDataUtility {

    public GPUEntity createTestGPU() {
        String modelNumber = "PRIME-RTX5070TI-O16G";
        String chip = "RTX 5070 Ti";
        String chipManufacturer = "NVIDIA";
        String boardManufacturer = "ASUS";
        String name = "Asus Prime GeForce RTX 5070 Ti OC 16G Graphics Card";

        return GPUEntity.builder()
                .modelNumber(modelNumber)
                .chip(chip)
                .chipManufacturer(chipManufacturer)
                .boardManufacturer(boardManufacturer)
                .name(name)
                .isActive(true)
                .build();
    }

    public RAMEntity createTestRAM() {
        String id = "KF560C36BBE2K2-64";
        String name = "Kingston 64GB (2x32GB) Fury Beast CL36 6000MHz DDR5 RAM (KF560C36BBE2K2-64)";
        String brand = "Kingston";
        String standard = "DDR5";
        String latency = "CL36";
        int volume = 64;
        int dimmCount = 2;
        int clockRate = 6000;
        Double voltage = 1.35;

        return RAMEntity.builder()
                .modelNumber(id)
                .name(name)
                .brand(brand)
                .standard(standard)
                .latency(latency)
                .volume(volume)
                .dimmCount(dimmCount)
                .clockRate(clockRate)
                .voltage(voltage)
                .build();
    }
}
