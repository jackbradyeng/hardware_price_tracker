package com.price_tracker.repositories.price_point_repos.jdbc_templates;

import static com.price_tracker.constants.other_constants.DatabaseConstants.DEFAULT_JDBC_BATCH_SIZE;
import com.price_tracker.domain.entities.price_point_entities.GenericPricePoint;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Timestamp;
import java.util.List;

/** A generic JDBC template for price point insertions. */
public class GenericPricePointJdbcTemplate<T extends GenericPricePoint> {

    private final JdbcTemplate jdbcTemplate;
    private final String sequenceName;
    private final String tableName;

    public GenericPricePointJdbcTemplate(JdbcTemplate jdbcTemplate, String sequenceName, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.sequenceName = sequenceName;
        this.tableName = tableName;
    }

    // all or nothing - no partial-insertions allowed
    @Transactional
    public void batchInsertPricePoints(List<T> pricePoints) {

        // fetch batch of IDs from the DB -> this avoids round-trips for each one
        String idQuery = "SELECT NEXTVAL('" + sequenceName + "') FROM GENERATE_SERIES(1, ?)";
        List<Long> ids = jdbcTemplate.queryForList(idQuery, Long.class, pricePoints.size());

        // assign the IDs to each price point instance
        for (int i = 0; i < pricePoints.size(); i++) {
            pricePoints.get(i).setId(ids.get(i));
        }

        // execute the batch insert
        String sql = "INSERT INTO " + tableName + " (id, model_number, vendor, currency, price, scraped_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                pricePoints,
                DEFAULT_JDBC_BATCH_SIZE,
                (ps, pricePoint) -> {
                    ps.setLong(1, pricePoint.getId());
                    ps.setString(2, pricePoint.getModelNumber());
                    ps.setString(3, pricePoint.getVendor());
                    ps.setString(4, pricePoint.getCurrency());
                    ps.setBigDecimal(5, pricePoint.getPrice());
                    ps.setTimestamp(6, Timestamp.valueOf(pricePoint.getScrapedAt()));
                }
        );
    }
}