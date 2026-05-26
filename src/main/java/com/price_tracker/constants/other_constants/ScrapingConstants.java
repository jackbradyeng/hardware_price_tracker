package com.price_tracker.constants.other_constants;

public final class ScrapingConstants {

    // SCRAPING SLEEP CONSTANT - AVOIDS OVERWHELMING TARGET SITES WITH TRAFFIC
    public static final Integer SLEEPING_CONSTANT = 500;

    // CRON TIMESTAMPS
    public static final String GPU_SCRAPING_TIME = "0 00 22 * * ?";
    public static final String RAM_SCRAPING_TIME = "0 5 22 * * ?";
    public static final String CPU_SCRAPING_TIME = "0 10 22 * * ?";
    public static final String GPU_WORKSTATION_SCRAPING_TIME = "0 15 22 * * ?";
    public static final String HDD_SCRAPING_TIME = "0 20 22 * * ?";
    public static final String SSD_SCRAPING_TIME = "0 25 22 * * ?";
    public static final String NVME_SCRAPING_TIME = "0 30 22 * * ?";
}
