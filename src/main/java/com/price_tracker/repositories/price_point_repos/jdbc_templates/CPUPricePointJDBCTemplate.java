package com.price_tracker.repositories.price_point_repos.jdbc_templates;

import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class CPUPricePointJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;

    public CPUPricePointJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // all or nothing - no partial-insertions allowed
    @Transactional
    public void batchInsertPricePoints(List<CPUPricePoint> pricePoints) {

        // fetch batch of IDs from the DB -> this avoids round-trips for each one
        String idQuery = "SELECT nextval('cpu_price_sequence') FROM generate_series(1, ?)";
        List<Long> ids = jdbcTemplate.queryForList(idQuery, Long.class, pricePoints.size());

        // assign the IDs to each price point instance
        for (int i = 0; i < pricePoints.size(); i++) {
            pricePoints.get(i).setId(ids.get(i));
        }

        // execute the batch insert
        String sql = "INSERT INTO CPU_PRICE_HISTORY (id, model_number, vendor, currency, price, scraped_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        int batchSize = 50;

        jdbcTemplate.batchUpdate(
                sql,
                pricePoints,
                batchSize,
                (ps, cpuPricePoint) -> {
                    ps.setLong(1, cpuPricePoint.getId());
                    ps.setString(2, cpuPricePoint.getModelNumber());
                    ps.setString(3, cpuPricePoint.getVendor());
                    ps.setString(4, cpuPricePoint.getCurrency());
                    ps.setBigDecimal(5, cpuPricePoint.getPrice());
                    ps.setTimestamp(6, Timestamp.valueOf(cpuPricePoint.getScrapedAt()));
                }
        );
    }
}
