package com.price_tracker.dao.implement;

import org.springframework.jdbc.core.JdbcTemplate;

public class RAMDataAccess {

    private final JdbcTemplate jdbcTemplate;

    public RAMDataAccess(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
