package com.price_tracker.repositories.price_point_repos.jdbc_templates;

import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class RAMPricePointJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;

    public RAMPricePointJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void batchInsertPricePoints(List<RAMPricePoint> pricePoints) {

        // fetch batch of IDs from the DB -> this avoids round-trips for each one
        String idQuery = "SELECT nextval('ram_price_sequence') FROM generate_series(1, ?)";
        List<Long> ids = jdbcTemplate.queryForList(idQuery, Long.class, pricePoints.size());

        // assign the IDs to each price point instance
        for (int i = 0; i < pricePoints.size(); i++) {
            pricePoints.get(i).setId(ids.get(i));
        }

        // execute the batch insert
        String sql = "INSERT INTO RAM_PRICE_HISTORY (id, model_number, vendor, currency, price, scraped_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        int batchSize = 50;

        jdbcTemplate.batchUpdate(
                sql,
                pricePoints,
                batchSize,
                (ps, ramPricePoint) -> {
                    ps.setLong(1, ramPricePoint.getId());
                    ps.setString(2, ramPricePoint.getModelNumber());
                    ps.setString(3, ramPricePoint.getVendor());
                    ps.setString(4, ramPricePoint.getCurrency());
                    ps.setBigDecimal(5, ramPricePoint.getPrice());
                    ps.setTimestamp(6, Timestamp.valueOf(ramPricePoint.getScrapedAt()));
                }
        );
    }
}
