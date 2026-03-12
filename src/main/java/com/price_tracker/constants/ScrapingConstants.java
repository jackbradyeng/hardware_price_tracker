package com.price_tracker.constants;

public final class ScrapingConstants {

    // may need to tighten the sleeping interval as the volume of scraped data increases...
    public static final Integer SLEEPING_CONSTANT = 750;

    // CRON timestamps
    public static final String GPU_SCRAPING_TIME = "0 00 22 * * ?";
    public static final String RAM_SCRAPING_TIME = "0 20 22 * * ?";
    public static final String CPU_SCRAPING_TIME = "0 40 22 * * ?";
}
