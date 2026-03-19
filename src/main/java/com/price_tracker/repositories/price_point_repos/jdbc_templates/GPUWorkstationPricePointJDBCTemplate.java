package com.price_tracker.repositories.price_point_repos.jdbc_templates;

import com.price_tracker.domain.entities.price_point_entities.GPUWorkstationPricePoint;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class GPUWorkstationPricePointJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;

    public GPUWorkstationPricePointJDBCTemplate(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Transactional
    public void batchInsertPricePoints(List<GPUWorkstationPricePoint> pricePoints) {

        // fetch batch of IDs from the DB -> this avoids round-trips for each one
        String idQuery = "SELECT nextval('ws_price_sequence') FROM generate_series(1, ?)";
        List<Long> ids = jdbcTemplate.queryForList(idQuery, Long.class, pricePoints.size());

        // assign the IDs to each price point instance
        for (int i = 0; i < pricePoints.size(); i++) {
            pricePoints.get(i).setId(ids.get(i));
        }

        // execute the batch insert
        String sql = "INSERT INTO WS_GPU_PRICE_HISTORY (id, model_number, vendor, currency, price, scraped_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        int batchSize = 50;

        jdbcTemplate.batchUpdate(
                sql,
                pricePoints,
                batchSize,
                (ps, GPUWorkstationPricePoint) -> {
                    ps.setLong(1, GPUWorkstationPricePoint.getId());
                    ps.setString(2, GPUWorkstationPricePoint.getModelNumber());
                    ps.setString(3, GPUWorkstationPricePoint.getVendor());
                    ps.setString(4, GPUWorkstationPricePoint.getCurrency());
                    ps.setBigDecimal(5, GPUWorkstationPricePoint.getPrice());
                    ps.setTimestamp(6, Timestamp.valueOf(GPUWorkstationPricePoint.getScrapedAt()));
                }
        );
    }
}
