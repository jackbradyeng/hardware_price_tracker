# Hardware Price Tracker (Backend)

A Spring Boot CRUD API designed to track computer hardware prices over time and across multiple vendors. Built for flexibility across hardware categories and vendor sources.

---

## Overview

This application maintains a catalogue of computer hardware products (CPUs, GPUs, RAM, GPU Workstations, HDDs, SSDs, NVMEs, etc.) and stores their price points from vendors as time-series data, enabling price trend analysis over time.

Importantly, it doesn't perform the scraping function itself. That is the role of a separate repository, `price_tracker_scraping_microservice`, which pulls product URLs from this application and pushes scraped price points back to it (see [Scraping Moved to a Separate Service](#scraping-moved-to-a-separate-service)). This app functions as the API that both the scraper and the frontend consume.

**Key capabilities:**
- Full CRUD API for hardware product management across various product types
- Bulk price-point CREATE endpoint, backed by a batch JDBC insert, for the scraper to write results to (ADMIN only)
- Vendor product-link GET endpoints exposing active product URLs for the scraper to pull work from
- Price point history stored per product and vendor, exposed as paginated time-series data
- Admin-gated writes (HTTP Basic + `ROLE_ADMIN`) with public reads

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 4.0.2 (Java 21) |
| Database | PostgreSQL |
| ORM | Spring Data JPA (standard queries), JDBC Template (bulk inserts) |
| Security | Spring Security (HTTP Basic, BCrypt) |
| Mapping | ModelMapper 3.0.0 |
| Validation | Jakarta Bean Validation |
| Env Config | Java Dotenv |
| Build | Maven |
| Testing | JUnit 5, Spring Boot Test (integration), Mockito, AssertJ |
| Dev Infrastructure | Docker / docker-compose |

---

## Architecture

The application follows a layered architecture with seven hardware product domains (CPU, GPU, RAM, GPUWorkstation, HDD, SSD, NVME), each with identical structure, scraped across two vendors (Umart, Scorptec):

```
Controller → Service (interface + implementation) → Repository (JPA + generic JDBC) → PostgreSQL
```

```
src/main/java/com/priceTracker/
├── controllers/
│   ├── productControllers/
│   ├── pricePointControllers/
│   └── vendorControllers/
├── services/
│   ├── productServices/
│   ├── vendorServices/
│   └── pricePointServices/
├── repositories/
│   ├── productRepositories/
│   ├── vendorRepositories/
│   ├── userRepositories/
│   └── pricePointRepositories/
│       └── jdbcTemplates/
├── domain/
│   ├── entities/
│   │   ├── productEntities/
│   │   ├── pricePointEntities/
│   │   ├── vendorEntities/
│   │   └── userEntities/
│   └── dto/
│       ├── hybridDTOs/
│       ├── hybridInterfaces/
│       ├── productDTOs/
│       ├── pricePointDTOs/
│       └── vendorDTOs/
├── mappers/
├── security/
├── advice/
├── config/
└── constants/
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

A set of HQL join queries relay composite product & price point DTOs to the front-end: 

```java
@Repository
public interface CPUPricePointRepository extends JpaRepository<CPUPricePoint, Long> {
    @Query(value = "select p as pricePoint, e as entity from CPUPricePoint p " +
            "left join CPUEntity e on p.modelNumber = e.modelNumber " +
            "where p.modelNumber = :modelNumber " +
            "order by p.scrapedAt desc",
            countQuery = "select count(p) from CPUPricePoint p where p.modelNumber = :modelNumber")
    Page<GenericDataAndPricePointProjection<CPUEntity, CPUPricePoint>> getPricePointsByModelNumber(
            @Param("modelNumber") String modelNumber, Pageable pageable);
}
```

Price point inserts, on the other hand, use a single generic JDBC template class, parameterised by entity type. Pricing data arrives in bulk daily, and delegating inserts to the ORM meant one sequence round-trip per row and no JDBC statement batching. The template instead pre-allocates a batch of sequence IDs in a single query, then batch-inserts:

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

### Scraping Moved to a Separate Service

This app used to scrape vendor sites itself, via per-vendor orchestrators on `@Scheduled` CRON jobs. That logic — orchestrators, vendor scraper implementations, CSS selector constants — has been extracted entirely into its own repository, `price_tracker_scraping_microservice`, and no longer exists here. This app's role has thus narrowed to two functions: i) tell the scraper what to scrape; and ii) validate and store what it finds.

The two repos talk over the REST API described in [API Endpoints](#api-endpoints), not a shared codebase or database:

1. **Pull** — the scraper calls the vendor `-page-links` endpoints (e.g. `GET /api/v1/umartproducts/gpu-page-links`) to get the current list of active product URLs for a vendor/category pair.
2. **Scrape** — it fetches and parses each page itself, entirely outside this app.
3. **Push** — it batches the results and calls the corresponding price point controller's bulk create endpoint (e.g. `POST /api/v1/gpu-pricepoints`) to persist them.

This decouples the two systems' scaling and deployment: this application no longer needs to run scraping work on any particular instance, so it can be scaled horizontally on API/DB load alone, while the scraper can be scheduled, retried, and scaled independently without a release here. The tradeoff is that what used to be one in-process call is now a network round trip with its own auth, validation, and failure modes — bulk-create payloads are validated with bean validation (`@NotBlank`/`@NotNull`/`@Positive` on `GenericPricePointDTO`) since they now originate outside the JVM, and writes are gated behind `ROLE_ADMIN` HTTP Basic auth.

### Generic Mapper

Entity ↔ DTO mapping is handled by a single `Mapper<A, B>` interface backed by `GenericMapper<A, B>`, which wraps `ModelMapper`. A `MapperFactory` bean constructs a `GenericMapper` for any entity/DTO pair on demand, so services no longer need a hand-written mapper class per domain:

```java
public CPUServiceImpl(CPURepository cpuRepository, MapperFactory mapperFactory) {
    this.cpuMapper = mapperFactory.create(CPUEntity.class, CPUDTO.class);
}
```

### Centralised Constants

Magic values are defined in a flat `/constants/` package rather than being scattered throughout the codebase — currently database table/sequence names and vendor names. Vendor-site specifics like CSS selectors moved out with the scraper into `price_tracker_scraping_microservice`.

---

## API Endpoints

All endpoints are versioned under `/api/v1`. Reads (`GET`) are public; every other verb requires HTTP Basic auth with `ROLE_ADMIN`.

Each hardware category exposes the same RESTful interface. Using GPU as an example:

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/gpus` | Create a GPU |
| `POST` | `/api/v1/gpus/saveall` | Batch create GPUs |
| `GET` | `/api/v1/gpus` | List all GPUs |
| `GET` | `/api/v1/gpus/{id}` | Get GPU by model number |
| `PUT` | `/api/v1/gpus/{id}` | Full update |
| `PATCH` | `/api/v1/gpus/{id}` | Partial update |
| `DELETE` | `/api/v1/gpus/{id}` | Delete GPU |

Identical endpoints exist for `/api/v1/cpus`, `/api/v1/ram`, `/api/v1/workstation_gpus`, `/api/v1/hdds`, `/api/v1/ssds`, and `/api/v1/nvmes`.

### Price Point Endpoints (Paginated)

Using GPU as an example:

| Method | Endpoint                                | Description |
|--------|-----------------------------------------|-------------|
| `POST` | `/api/v1/gpu-pricepoints`               | Bulk create price points (admin only; batch JDBC insert) |
| `GET` | `/api/v1/gpu-pricepoints`               | List all GPU price points (paginated) |
| `GET` | `/api/v1/gpu-pricepoints/{modelNumber}` | Get price history for a specific GPU (paginated) |

Both `GET` endpoints accept standard Spring `Pageable` query parameters with a default page size of 30:

| Parameter | Description | Example |
|-----------|-------------|---------|
| `page` | Zero-based page index | `?page=0` |
| `size` | Number of results per page (default: 30) | `?size=50` |
| `sort` | Sort field and direction | `?sort=scrapedAt,desc` |

Responses are wrapped in a Spring `Page<T>` envelope with `content`, `totalElements`, `totalPages`, `number`, and `size` fields.

Identical endpoints exist for `/api/v1/cpu-pricepoints`, `/api/v1/ram-pricepoints`, `/api/v1/workstation-gpu-pricepoints`, `/api/v1/hdd-pricepoints`, `/api/v1/ssd-pricepoints`, and `/api/v1/nvme-pricepoints`.

### Vendor Endpoints

`/api/v1/vendors` exposes CRUD for the vendors table. Each vendor also has its own product-mapping endpoint, linking a product's model number to the URL used to track it (`/api/v1/umartproducts`, `/api/v1/scorptecproducts`), supporting the same create/batch-create/list/get/update/delete operations as the product endpoints above.

Each vendor-product controller additionally exposes a set of read-only link endpoints, consumed by the separate `price_tracker_scraping_microservice` to discover which URLs to scrape next:

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/{vendor}products/cpu-page-links` | Active CPU product URLs |
| `GET` | `/api/v1/{vendor}products/gpu-page-links` | Active GPU product URLs |
| `GET` | `/api/v1/{vendor}products/workstation-gpu-page-links` | Active workstation GPU product URLs |
| `GET` | `/api/v1/{vendor}products/ram-page-links` | Active RAM product URLs |
| `GET` | `/api/v1/{vendor}products/hdd-page-links` | Active HDD product URLs |
| `GET` | `/api/v1/{vendor}products/ssd-page-links` | Active SSD product URLs |
| `GET` | `/api/v1/{vendor}products/nvme-page-links` | Active NVME product URLs |

...where `{vendor}` is `umart` or `scorptec`.

---

## Frontend Networking

CORS is currently configured for `localhost:3000`, although this is scheduled to be changed on cloud deployment. The CORS configuration allows the backend to communicate with the frontend - a separate repo called `hardware_price_tracker.fe`.

---

## Testing

Tests are organised into three layers, mirroring the application's layered architecture:

**Repository tests** (`repositories/`) — exercise JPA repositories directly against a real PostgreSQL instance, verifying that entities and vendor-product mappings can be persisted and retrieved correctly.

**Controller tests** (`controllers/`) — test REST endpoints via `MockMvc`, covering the full request/response cycle: status codes, response body shape, CRUD operations, pagination, and 404 handling for missing resources. This now includes the bulk price-point create endpoint (currently covered for CPU) and the vendor `-page-links` endpoints. A `MockMvcAdminAuthCustomizer` (`config/`) attaches admin HTTP Basic credentials to every request by default, since most controllers mix public `GET` endpoints with admin-only write endpoints.

**DTO validation unit tests** (`domain/dto/productDTOs/`) — assert that bean validation constraints on each product DTO reject invalid input, independent of the web layer.

Test data is centralised in per-domain utility classes under `testingData/` (e.g. `GPUTestingUtility`, `GPUTestingData`), plus per-vendor utilities, providing shared fixture creation methods reused across test layers.

**NOTE:** Tests use `@ActiveProfiles("test")` to switch to a `create-drop` database, and controller/repository tests run within a `@Transactional` context that rolls back after each test. This ensures full isolation without manual teardown. Liquibase is disabled for the `test` profile — Hibernate owns schema creation instead, since it would otherwise try to migrate the same schema on every context load.

---

## Getting Started (For Recruiters, Interviewers, & Contributors)

### Prerequisites

- Java 21
- Docker (for PostgreSQL)
- Maven (or use the included wrapper)

### Running Locally

`docker-compose.yml` defines two Postgres instances so local development never shares a database with production: `db` (port 5432, `production` profile) and `db-developer` (port 5433, `developer` profile).

Create a `secrets.env` (gitignored) alongside `docker-compose.yml` with:

```
DB_NAME=
DB_USERNAME=
DB_PASSWORD=
DEV_DB_NAME=
DEV_DB_USERNAME=
DEV_DB_PASSWORD=
ADMIN_USERNAME=
ADMIN_PASSWORD=
DEV_ADMIN_USERNAME=
DEV_ADMIN_PASSWORD=
```

Only the `DEV_*` values are needed to run locally, since the app defaults to the `developer` profile (`application.properties`); the plain `DB_*`/`ADMIN_*` values back the `production` profile.

```bash
# Start both Postgres instances
docker-compose --env-file secrets.env up

# Run the application (developer profile by default)
./mvnw spring-boot:run
```

Under the `developer` profile the app connects to `db-developer` and serves at `http://localhost:8082`. Under `production` it serves at `http://localhost:8080` against `db`.

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
