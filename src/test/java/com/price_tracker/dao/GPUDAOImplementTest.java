package com.price_tracker.dao;

import com.price_tracker.dao.implement.GPUDataAccess;
import com.price_tracker.domain.GPU;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GPUDAOImplementTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private GPUDataAccess testInstance;

    @Test
    public void testCreate() {
        String modelNumber = "PRIME-RTX5070TI-O16G";
        String name = "Asus Prime GeForce RTX 5070 Ti OC 16G Graphics Card";
        String chip = "NVIDIA";
        String board = "ASUS";
        int vram = 16;
        BigDecimal price = new BigDecimal("1598.00");

        GPU gpu = GPU.builder()
                .model_number(modelNumber)
                .name(name)
                .chipManufacturer(chip)
                .boardManufacturer(board)
                .videoMemory(vram)
                .price(price)
                .build();
        testInstance.create(gpu);

        verify(jdbcTemplate).update(eq("INSERT INTO GPUs (model_number, name, chip_manufacturer, board_manufacturer, video_memory, price)"),
                eq(modelNumber), eq(name), eq(chip), eq(board), eq(vram), eq(price));
    }
}
