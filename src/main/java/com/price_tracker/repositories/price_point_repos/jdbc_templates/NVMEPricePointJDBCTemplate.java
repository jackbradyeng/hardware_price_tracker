package com.price_tracker.repositories.price_point_repos.jdbc_templates;

import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class NVMEPricePointJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;

    public NVMEPricePointJDBCTemplate(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Transactional
    public void batchInsertPricePoints(List<NVMEPricePoint> pricePoints) {

        // fetch batch of IDs from the DB -> this avoids round-trips for each one
        String idQuery = "SELECT NEXTVAL('nvme_price_sequence') FROM GENERATE_SERIES(1, ?)";
        List<Long> ids = jdbcTemplate.queryForList(idQuery, Long.class, pricePoints.size());

        // assign the IDs to each price point instance
        for (int i = 0; i < pricePoints.size(); i++) {
            pricePoints.get(i).setId(ids.get(i));
        }

        // execute the batch insert
        String sql = "INSERT INTO NVME_PRICE_HISTORY (id, model_number, vendor, currency, price, scraped_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        int batchSize = 50;

        jdbcTemplate.batchUpdate(
                sql,
                pricePoints,
                batchSize,
                (ps, nvmePricePoint) -> {
                    ps.setLong(1, nvmePricePoint.getId());
                    ps.setString(2, nvmePricePoint.getModelNumber());
                    ps.setString(3, nvmePricePoint.getVendor());
                    ps.setString(4, nvmePricePoint.getCurrency());
                    ps.setBigDecimal(5, nvmePricePoint.getPrice());
                    ps.setTimestamp(6, Timestamp.valueOf(nvmePricePoint.getScrapedAt()));
                }
        );
    }
}