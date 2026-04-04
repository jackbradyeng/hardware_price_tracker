# Hardware Price Tracker

A Spring Boot REST API that tracks computer hardware prices over time by scraping vendor websites on a daily schedule. Built for flexibility across hardware categories and vendor sources.

---

## Overview

This application maintains a catalogue of computer hardware products (CPUs, GPUs, RAM, GPU Workstations, etc.) and records their price points from vendors daily. Price history is stored as time-series data, enabling price trend analysis over time.

**Key capabilities:**
- Full CRUD API for hardware product management across various product types
- Daily scheduled web scraping of vendor product pages
- Price point history stored per product and vendor
- Staggered scraping schedules to distribute load and avoid rate limiting

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 4.0.2 (Java 21) |
| Database | PostgreSQL |
| ORM | Spring Data JPA (standard queries), JDBC Template (bulk inserts) |
| Web Scraping | JSoup 1.15.3 |
| Mapping | ModelMapper 3.0.0 |
| Build | Maven |
| Testing | JUnit 5, Spring Boot Test (integration) |
| Dev Infrastructure | Docker / docker-compose |

---

## Architecture

The application follows a layered architecture with four hardware product domains (CPU, GPU, RAM, GPUWorkstation), each with identical structure:

```
Controller → Service (interface + implementation) → Repository (JPA + JDBC) → PostgreSQL
```

```
src/main/java/com/price_tracker/
├── controllers/
│   ├── product_controllers/        # REST endpoints per product type
│   ├── price_point_controllers/    # Price history endpoints
│   └── vendor_controllers/         # Vendor product mapping endpoints
├── services/                       # @Transactional business logic
├── repositories/
│   ├── product_repos/              # JPA repositories
│   └── price_point_repos/
│       └── jdbc_templates/         # Optimised batch insert templates
├── domain/
│   ├── entities/                   # JPA entities (products + price points)
│   ├── dto/                        # Request/response DTOs
│   │   └── hybrid_interfaces/      # JPA projection interfaces
│   └── ...
├── webscraper/
│   ├── orchestrators/              # Scheduled scraping jobs
│   ├── product_services/           # Scraper interfaces + implementations
│   └── vendor_templates/           # Abstract base class (GenericUmartScraper)
├── mappers/                        # Generic Mapper<A,B> interface + impls
├── config/                         # Spring bean configuration
└── constants/                      # CSS selectors, table names, cron schedules, URLs
```

---

## Design Decisions

### Hybrid Repository Pattern (JPA + JDBC)

Product entities use **JPA** for standard CRUD and custom HQL queries, including projection interfaces for joins. This is useful for communicating data to the frontend efficiently, where it is transformed into time-series charts:

```java
@Query("select p as GPUPricePoint, e as GPUEntity from GPUPricePoint p " +
       "left join GPUEntity e on p.modelNumber = e.modelNumber " +
       "where p.modelNumber = :modelNumber")
List<GPUDataAndPricePointProjection> getPricePointsByModelNumber(@Param("modelNumber") String modelNumber);
```

Price point inserts on the other hand use **JDBC Template** for performance. Pricing data arrives in bulk daily, so the JDBC template pre-allocates a batch of sequence IDs in a single round-trip before inserting. This implementation was conceived as a solution to the HIbernate N+1 problem:

```java
// Pre-allocate IDs in one query
String idQuery = "SELECT nextval('gpu_price_sequence') FROM generate_series(1, ?)";
List<Long> ids = jdbcTemplate.queryForList(idQuery, Long.class, pricePoints.size());

// Batch insert at size 50
jdbcTemplate.batchUpdate(sql, pricePoints, 50, (ps, point) -> { ... });
```

### Template Method Pattern for Scrapers

Each vendor has its own Generic Scraping class. For instance, `GenericUmartScraper` is the base class used for scraping prices from the popular Australian hardware vendor Umart Online. It handles JSoup connections, DOM parsing, model number extraction, and price normalisation. Concrete implementations (e.g. `UmartGPUScrapingService`) extend it to provide product-specific price point construction — reusing all the parsing logic without duplicating it.

### Orchestrator Pattern for Scheduled Jobs

Each product category has a dedicated orchestrator (`GPUScrapingOrchestrator`, etc.) annotated with `@Scheduled`. Orchestrators fetch the list of active product URLs from the DB, stream them through the scraper, filter out any failures, and persist results in a single batch:

```java
List<GPUPricePoint> pricePoints = umartProductRepository.findUrlsForActiveGPUs()
    .stream()
    .map(this::processGPU)
    .flatMap(Optional::stream)   // silently drops individual URL failures
    .toList();
```

### Centralised Constants

All magic values are defined in a `/constants/` package rather than scattered through the codebase. This includes CSS selectors, vendor URLs, database table and sequence names, and CRON expressions. Adapting to a vendor site change means updating one file.

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

Identical endpoints exist for `/api/cpus`, `/api/rams`, and `/api/gpu-workstations`.

Price point history endpoints follow the same pattern under `/api/gpu-price-points`, etc.

CORS is configured for `localhost:3000`. This enables communication with the frontend - a separate repo called hardware_price_tracker.fe.

---

## Testing

All tests are integration tests — there are no unit tests with mocked dependencies. Every test spins up the full Spring context against a real PostgreSQL instance and exercises the actual beans end-to-end.

Tests are organised into three layers, mirroring the application's layered architecture:

**Repository tests** (`repositories/`) — exercises JPA repositories directly, verifying that entities can be persisted and retrieved correctly from the database.

**Controller tests** (`controllers/`) — tests REST endpoints via `MockMvc`, covering the full request/response cycle (status codes, response body shape, CRUD operations, and 404 handling for missing resources).

**Scraper tests** (`scrapers/`) — the most comprehensive layer. These tests hit live vendor URLs to verify that the scraper extracts the expected model number, then persist price points via the JDBC batch template and assert on the returned data through the price point API. Batch insertion correctness is verified across both small (10 items) and large (110 items, spanning multiple round-trips) insertion counts.

**Test data** is centralised in per-domain utility classes (`testing_data/`), e.g. `GPUTestingUtility` and `GPUTestingData`, which provide shared fixture creation methods used across all three test layers.

**NOTE:** Tests use `@DirtiesContext` to rebuild the schema between test methods, and `@ActiveProfiles("test")` to switch to a `create-drop` database. This ensures full isolation without manual teardown.

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
