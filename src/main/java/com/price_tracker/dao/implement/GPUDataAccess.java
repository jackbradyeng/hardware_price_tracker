package com.price_tracker.dao.implement;

import java.util.Optional;
import com.price_tracker.dao.GPUDAO;
import com.price_tracker.domain.GPU;
import org.springframework.jdbc.core.JdbcTemplate;

public class GPUDataAccess implements GPUDAO {

    private final JdbcTemplate jdbcTemplate;

    public GPUDataAccess(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(GPU gpu) {
        jdbcTemplate.update("INSERT INTO GPUs (model_number, name, chip_manufacturer, board_manufacturer, video_memory, price)",
        gpu.getModel_number(), gpu.getName(), gpu.getChipManufacturer(), gpu.getBoardManufacturer(), gpu.getVideoMemory(), gpu.getPrice());
    }

    @Override
    public Optional<GPU> find(String modelNumber) {
        return Optional.empty();
    }
}
