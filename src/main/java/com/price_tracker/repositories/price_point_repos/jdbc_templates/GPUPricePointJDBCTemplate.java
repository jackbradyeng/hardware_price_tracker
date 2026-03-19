package com.price_tracker.repositories.price_point_repos.jdbc_templates;

import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class GPUPricePointJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;

    public GPUPricePointJDBCTemplate(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    // all or nothing - no partial-insertions allowed
    @Transactional
    public void batchInsertPricePoints(List<GPUPricePoint> pricePoints) {

        // fetch batch of IDs from the DB -> this avoids round-trips for each one
        String idQuery = "SELECT nextval('gpu_price_sequence') FROM generate_series(1, ?)";
        List<Long> ids = jdbcTemplate.queryForList(idQuery, Long.class, pricePoints.size());

        // assign the IDs to each price point instance
        for (int i = 0; i < pricePoints.size(); i++) {
            pricePoints.get(i).setId(ids.get(i));
        }

        // execute the batch insert
        String sql = "INSERT INTO GPU_PRICE_HISTORY (id, model_number, vendor, currency, price, scraped_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        int batchSize = 50;

        jdbcTemplate.batchUpdate(
                sql,
                pricePoints,
                batchSize,
                (ps, gpuPricePoint) -> {
                    ps.setLong(1, gpuPricePoint.getId());
                    ps.setString(2, gpuPricePoint.getModelNumber());
                    ps.setString(3, gpuPricePoint.getVendor());
                    ps.setString(4, gpuPricePoint.getCurrency());
                    ps.setBigDecimal(5, gpuPricePoint.getPrice());
                    ps.setTimestamp(6, Timestamp.valueOf(gpuPricePoint.getScrapedAt()));
                }
        );
    }
}
