package com.price_tracker.dao.implement;

import java.util.Optional;
import com.price_tracker.dao.RAMDAO;
import com.price_tracker.domain.RAM;
import org.springframework.jdbc.core.JdbcTemplate;

public class RAMDataAccess implements RAMDAO {

    private final JdbcTemplate jdbcTemplate;

    public RAMDataAccess(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(RAM ram) {
        jdbcTemplate.update("INSERT INTO RAM (id, name, brand, volume, clock_rate)", ram.getId(), ram.getName(), ram.getBrand(),
                ram.getVolume(), ram.getClockRate());
    }

    @Override
    public Optional<RAM> find(String id) {
        return Optional.empty();
    }
}
