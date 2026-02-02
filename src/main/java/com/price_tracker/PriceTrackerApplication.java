package com.price_tracker;

import javax.sql.DataSource;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@Log
public class PriceTrackerApplication implements CommandLineRunner {

    public final DataSource dataSource;

    // constructor
    public PriceTrackerApplication(final DataSource dataSource) { this.dataSource = dataSource; }

    @Override
    public void run(final String... args) {
        log.info("Datasource: " + dataSource.toString());
        final JdbcTemplate restTemplate = new JdbcTemplate(dataSource);
        restTemplate.execute("select 1");
    }

	public static void main(String[] args) {
		SpringApplication.run(PriceTrackerApplication.class, args);
	}

}
