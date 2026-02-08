package com.price_tracker.repositories;

import com.price_tracker.domain.entities.GPU;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TestDataUtility {

    public GPU createTestGPU() {
        String modelNumber = "PRIME-RTX5070TI-O16G";
        String name = "Asus Prime GeForce RTX 5070 Ti OC 16G Graphics Card";
        String chip = "NVIDIA";
        String board = "ASUS";
        int vram = 16;
        BigDecimal price = new BigDecimal("1598.00");

        GPU gpu = GPU.builder()
                .modelNumber(modelNumber)
                .name(name)
                .chipManufacturer(chip)
                .boardManufacturer(board)
                .videoMemory(vram)
                .price(price)
                .build();
        return gpu;
    }
}
