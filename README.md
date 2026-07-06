# Hardware Price Tracker (Backend)

A Spring Boot application which tracks computer hardware prices over time by scraping popular vendor websites on a daily schedule. Built for flexibility across hardware categories and vendor sources.

---

## Overview

This application maintains a catalogue of computer hardware products (CPUs, GPUs, RAM, GPU Workstations, HDDs, SSDs, NVMEs, etc.) and records their price points from vendors daily. Pricing history is stored as time-series data, enabling price trend analysis over time.

**Key capabilities:**
- Full CRUD API for hardware product management across various product types
- Daily scheduled web scraping across multiple vendors & product categories using a CRON schedule
- Price point history stored per product and vendor
- Staggered scraping schedules to distribute load and avoid rate limiting

---

## Tech Stack

| Layer | Technology                                                       |
|-------|------------------------------------------------------------------|
| Framework | Spring Boot 4.0.2 (Java 21)                                      |
| Database | PostgreSQL                                                       |
| ORM | Spring Data JPA (standard queries), JDBC Template (bulk inserts) |
| Web Scraping | JSoup 1.15.3                                                     |
| Mapping | ModelMapper 3.0.0                                                |
| Env Config | Java Dotenv                                                      |
| Build | Maven                                                            |
| Testing | JUnit 5, Spring Boot Test (integration), Mockito, AssertJ        |
| Dev Infrastructure | Docker / docker-compose                                          |

---

## Architecture

The application follows a layered architecture with seven hardware product domains (CPU, GPU, RAM, GPUWorkstation, HDD, SSD, NVME), each with identical structure, scraped across two vendors (Umart, Scorptec):

```
Controller → Service (interface + implementation) → Repository (JPA + generic JDBC) → PostgreSQL
```

```
src/main/java/com/price_tracker/
├── controllers/
│   ├── product_controllers/        # REST API endpoints per product type
│   ├── price_point_controllers/    # Price history endpoints
│   └── vendor_controllers/         # Vendor & vendor-product mapping endpoints
├── services/
├── repositories/
│   ├── product_repos/              # JPA repositories, one per product type
│   ├── vendor_repos/               # JPA repositories for vendors & vendor products
│   └── price_point_repos/
│       └── jdbc_templates/         # Generic batch insert template (shared across all domains)
├── domain/
│   ├── entities/
│   │   ├── product_entities/       # JPA entities per product
│   │   ├── price_point_entities/   # GenericPricePoint (mapped superclass) + per-domain subclasses
│   │   └── vendor_entities/        # Vendor & vendor-product entities
│   ├── dto/                        # Request/response DTOs
│   │   └── hybrid_interfaces/      # Generic JPA projection interface
│   └── ...
├── webscraper/
│   ├── orchestrators/              # Scheduled scraping jobs, split by vendor
│   │   ├── umart_orchestrators/
│   │   └── scorptec_orchestrators/
│   ├── product_services/           # Generic service that builds price point DTOs
│   └── vendor_templates/
│       ├── GenericVendorScraper.java   # Shared scraper contract
│       └── impl/                       # One implementation per vendor
├── mappers/                        # Generic Mapper<A,B> interface + impls
├── config/                         # Spring bean configuration
└── constants/                      # CSS selectors, table names, cron schedules, URLs
```

---

## Design Decisions

### Hybrid Repository Pattern (JPA + Generic JDBC)

Product entities use **JPA** for standard CRUD and custom HQL queries, including a generic projection interface for joins between a product and its price history. This is useful for communicating data to the frontend efficiently, where it is transformed into time-series charts:

```java
public interface GenericDataAndPricePointProjection<E, P> {
    E getEntity();
    P getPricePoint();
}
```

Price point inserts, on the other hand, use a single **generic JDBC template** class, parameterised by entity type, for performance. Pricing data arrives in bulk daily, so the template pre-allocates a batch of sequence IDs in one round-trip before inserting. This implementation was conceived as a solution to the Hibernate N+1 problem:

```java
public class GenericPricePointJdbcTemplate<T extends GenericPricePoint> {

    @Transactional
    public void batchInsertPricePoints(List<T> pricePoints) {
        // Pre-allocate IDs in one query
        String idQuery = "SELECT NEXTVAL('" + sequenceName + "') FROM GENERATE_SERIES(1, ?)";
        List<Long> ids = jdbcTemplate.queryForList(idQuery, Long.class, pricePoints.size());
        // ...assign IDs, then batch insert at DEFAULT_JDBC_BATCH_SIZE
    }
}
```

A single Spring configuration class wires up one bean per product domain (e.g. a `GPUPricePoint` template), each pointed at its own sequence and table name from a centralised constants file — replacing what was previously seven hand-written, near-identical template classes.

### Price Point Entity Inheritance

Price point entities share a common `GenericPricePoint` `@MappedSuperclass` holding `modelNumber`, `vendor`, `currency`, `price`, and `scrapedAt`. Each domain (`GPUPricePoint`, `CPUPricePoint`, etc.) extends it and declares only its own `@Id`/`@SequenceGenerator`, since Spring Data JPA doesn't allow a mapped superclass to define those directly.

### Generic Scraper Contract

Rather than one scraper class per vendor *per product*, there is a single scraper implementation per vendor. Each implementation handles JSoup connections, DOM parsing, model number/price refinement, and delegates the CSS selectors for a given product to the caller — so the same scraper instance is reused across all seven product domains for that vendor.

### Orchestrator Pattern for Scheduled Jobs

Each vendor/product combination has a dedicated orchestrator annotated with `@Scheduled`, grouped into per-vendor packages. All orchestrators implement a shared `GenericScrapingOrchestrator` interface, which provides a default method for scraping + sleeping + mapping a single product URL into a price point DTO:

```java
List<GPUPricePoint> pricePoints = umartProductRepository.findUrlsForActiveGPUs()
    .stream()
    .map(url -> processPricePoint(umartProductScraper, genericScrapingService, UMART_SLEEPING_CONSTANT,
            url, UMART_CSS_MODEL_LOCATION, UMART_CSS_PRICE_LOCATION, UMART, AUD))
    .flatMap(Optional::stream)
    .map(pricePointMapper::mapFrom)
    .toList();

gpuGenericPricePointJDBCTemplate.batchInsertPricePoints(pricePoints);
```

### Generic Mapper

Entity ↔ DTO mapping is handled by a single `Mapper<A, B>` interface backed by `GenericMapper<A, B>`, which wraps `ModelMapper`. A `MapperFactory` bean constructs a `GenericMapper` for any entity/DTO pair on demand, so services no longer need a hand-written mapper class per domain:

```java
public CPUServiceImpl(CPURepository cpuRepository, MapperFactory mapperFactory) {
    this.cpuMapper = mapperFactory.create(CPUEntity.class, CPUDTO.class);
}
```

### Centralised Constants

All magic values are defined in a `/constants/` package rather than scattered through the codebase. This includes CSS selectors, vendor URLs, database table/sequence names, JDBC batch size, and CRON expressions. Adapting to a vendor site change means updating one file.

---

## API Endpoints

Each hardware category exposes the same RESTful interface. Using GPU as an example:

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/gpus` | Create a GPU |
| `POST` | `/api/gpus/saveall` | Batch create GPUs |
| `GET` | `/api/gpus` | List all GPUs |
| `GET` | `/api/gpus/{id}` | Get GPU by model number |
| `PUT` | `/api/gpus/{id}` | Full update |
| `PATCH` | `/api/gpus/{id}` | Partial update |
| `DELETE` | `/api/gpus/{id}` | Delete GPU |

Identical endpoints exist for `/api/cpus`, `/api/rams`, `/api/workstation_gpus`, `/api/hdds`, `/api/ssds`, and `/api/nvmes`.

Price point history endpoints follow the same pattern under `/api/gpu_pricepoints`, etc.

### Price Point Endpoints (Paginated)

Price point endpoints return paginated responses. Using GPU as an example:

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/gpu_pricepoints` | List all GPU price points (paginated) |
| `GET` | `/api/gpu_pricepoints/{modelNumber}` | Get price history for a specific GPU (paginated) |

Both endpoints accept standard Spring `Pageable` query parameters with a default page size of 30:

| Parameter | Description | Example |
|-----------|-------------|---------|
| `page` | Zero-based page index | `?page=0` |
| `size` | Number of results per page (default: 30) | `?size=50` |
| `sort` | Sort field and direction | `?sort=scrapedAt,desc` |

Responses are wrapped in a Spring `Page<T>` envelope with `content`, `totalElements`, `totalPages`, `number`, and `size` fields.

Identical paginated endpoints exist for `/api/cpu_pricepoints`, `/api/ram_pricepoints`, `/api/workstation_gpu_pricepoints`, `/api/hdd_pricepoints`, `/api/ssd_pricepoints`, and `/api/nvme_pricepoints`.

### Vendor Endpoints

`/api/vendors` exposes CRUD for the vendors table. Each vendor also has its own product-mapping endpoint, linking a product's model number to the scraped URL used to track it (e.g. `/api/umartproducts`, `/api/scorptecproducts`), each supporting the same create/batch-create/list/get/update/delete operations as the product endpoints above.

---

## Frontend Networking

CORS is configured for `localhost:3000`. This enables communication with the frontend - a separate repo called hardware_price_tracker.fe.

---

## Testing

Tests are organised into three layers, mirroring the application's layered architecture, plus a dedicated scraper test split:

**Repository tests** (`repositories/`) — exercises JPA repositories directly, verifying that entities can be persisted and retrieved correctly from the database.

**Controller tests** (`controllers/`) — tests REST endpoints via `MockMvc`, covering the full request/response cycle (status codes, response body shape, CRUD operations, and 404 handling for missing resources).

**Scraper unit tests** (`scrapers/unit_tests/`) — run offline against static HTML fixtures checked into test resources, verifying that each vendor scraper extracts the expected model number and correctly refines prices/model numbers, without hitting a live site or a database.

**Scraper integration tests** (`scrapers/integration_tests/`) — spin up the full Spring context against a real PostgreSQL instance, persist price points via the generic JDBC batch template, and assert on the returned data through the price point API. Batch insertion correctness is verified across both small (10 items) and large (110 items, spanning multiple round-trips) insertion counts.

Static HTML fixtures are captured on demand by a manually-run capture tool that hits live vendor URLs, and loaded in unit tests from disk. Fixtures are re-captured whenever a vendor changes their page markup.

**Test data** is centralised in per-domain utility classes (`testing_data/`), e.g. `GPUTestingUtility` and `GPUTestingData`, plus per-vendor utilities, which provide shared fixture creation methods used across all test layers.

**NOTE:** Tests use `@ActiveProfiles("test")` to switch to a `create-drop` database, and controller/repository tests run within a `@Transactional` context that rolls back after each test. This ensures full isolation without manual teardown.

---

## Getting Started

### Prerequisites

- Java 21
- Docker (for PostgreSQL)
- Maven (or use the included wrapper)

### Running Locally

```bash
# Start PostgreSQL
docker-compose up

# Set environment variables (or create a .env file)
export DB_NAME=your_db_name
export DB_PASSWORD=your_password

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

### Running Tests

Tests require a running PostgreSQL instance with the credentials from `src/test/resources/application-test.properties` (`testing_db` / `test_user` / `test_password`).

```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=GPUEntityControllerIntegrationTests
```

### Building

```bash
./mvnw clean install
```