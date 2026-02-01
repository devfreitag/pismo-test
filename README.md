# Pismo Test

A Spring Boot REST API for managing financial transactions and accounts. This application implements a transaction routing system that automatically adjusts transaction amounts based on operation types (debits vs credits).

## Tech Stack

- **Java**: 21 (Eclipse Temurin)
- **Spring Boot**: 4.0.2
- **PostgreSQL**: 16 Alpine
- **Flyway**: Database migration tool
- **Docker & Docker Compose**: Containerization
- **Maven**: Build tool
- **Lombok**: Code generation
- **SpringDoc OpenAPI**: 3.0.0 (Swagger documentation)
- **H2 Database**: In-memory database for testing

## Prerequisites

- Java 21 or higher
- Maven 3.x
- Docker & Docker Compose

## How to...

1. Execute tests:
   ```bash
   ./mvnw test
   ```
2. Build and start the application:
   ```bash 
   ./mvnw clean install
   docker-compose up --build
   ```
   This will:
   - Start PostgreSQL on port 5432
   - Build and start the application on port 8080
   - Automatically run database migrations
3. Access the documentations:
   - http://localhost:8080/swagger-ui/index.html
   - http://localhost:8080/v3/api-docs
4. Stop the services:
   ```bash
   docker-compose down
   ```

## Database Migrations

Flyway handles database versioning. Migration scripts are in `src/main/resources/db/migration/`. Migrations run automatically on startup.

## Project Structure

```
src/
├── main/
│   ├── java/com/devfreitag/pismotest/
│   │   ├── api/              # API interfaces with Swagger annotations
│   │   ├── api/controllers/  # REST controllers
│   │   ├── entities/         # JPA entities
│   │   ├── enums/           # Operation type enumeration
│   │   ├── exceptions/      # Custom exceptions
│   │   ├── models/          # Request/Response DTOs
│   │   ├── repositories/    # Spring Data JPA repositories
│   │   └── services/        # Business logic
│   └── resources/
│       ├── application.properties
│       └── db/migration/    # Flyway migrations
└── test/                    # Tests
```

