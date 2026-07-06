package com.price_tracker.constants.other_constants;

public final class DatabaseConstants {

    // PRODUCT TABLES
    public static final String GPU_TABLE_NAME = "GPUs";
    public static final String WORKSTATION_GPU_TABLE_NAME = "WS_GPUs";
    public static final String RAM_TABLE_NAME = "RAM";
    public static final String CPU_TABLE_NAME = "CPUs";
    public static final String HDD_TABLE_NAME = "HDDs";
    public static final String SSD_TABLE_NAME = "SSDs";
    public static final String NVME_TABLE_NAME = "NVMEs";

    // PRICE POINT TABLES
    public static final String GPU_PRICE_HISTORY = "GPU_PRICE_HISTORY";
    public static final String WORKSTATION_GPU_PRICE_HISTORY = "WS_GPU_PRICE_HISTORY";
    public static final String RAM_PRICE_HISTORY = "RAM_PRICE_HISTORY";
    public static final String CPU_PRICE_HISTORY = "CPU_PRICE_HISTORY";
    public static final String HDD_PRICE_HISTORY = "HDD_PRICE_HISTORY";
    public static final String SSD_PRICE_HISTORY = "SSD_PRICE_HISTORY";
    public static final String NVME_PRICE_HISTORY = "NVME_PRICE_HISTORY";

    // VENDORS
    public static final String VENDOR_TABLE_NAME = "VENDORS";
    public static final String UMART_PRODUCT_NAME = "UMART_PRODUCTS";
    public static final String SCORPTEC_PRODUCT_NAME = "SCORPTEC_PRODUCTS";

    // SEQUENCES
    public static final String GPU_PRICE_SEQUENCE = "gpu_price_sequence";
    public static final String WS_GPU_PRICE_SEQUENCE = "ws_price_sequence";
    public static final String RAM_PRICE_SEQUENCE = "ram_price_sequence";
    public static final String CPU_PRICE_SEQUENCE = "cpu_price_sequence";
    public static final String HDD_PRICE_SEQUENCE = "hdd_price_sequence";
    public static final String SSD_PRICE_SEQUENCE = "ssd_price_sequence";
    public static final String NVME_PRICE_SEQUENCE = "nvme_price_sequence";

    // JDBC BATCH SIZE FOR AUTOMATED PRICE POINT INSERTIONS
    public static final Integer DEFAULT_JDBC_BATCH_SIZE = 50;
}
