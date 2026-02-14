# URL Shortener Microservice

A high-performance URL-Shortener microservice developed as a REST Service using Java and the Spring Boot framework. This service allows users to convert long URLs into shortened, manageable codes, resolve them back to the original URLs, and track usage statistics.

## Features

- **URL Shortening**: Generate unique 6-character short codes for any valid long URL.
- **URL Resolution**: Automatically redirect from short URLs to the original destination.
- **Metadata & Analytics**: Track click counts, creation dates, and expiration status for each short code.
- **Metrics & Monitoring**: Integrated with Spring Boot Actuator and Prometheus for real-time monitoring.
- **Validation**: Robust validation for input URLs and short codes.
- **Error Handling**: Graceful handling of expired or non-existent URLs.

## Prerequisites

To run this project, you need:

- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL** (for persistent storage)

## Getting Started

### 1. Database Setup

Ensure you have a PostgreSQL instance running. Create a database named `url_shortener_dev` for development or use your own and update the configuration.

### 2. Configuration

The application uses Spring Boot profiles. You can find configuration files in `src/main/resources`:

- `application.yaml`: Common settings.
- `application-development.yml`: Development settings (uses `url_shortener_dev`).
- `application-production.yml`: Production settings.

Ensure the following environment variables are set (or update the YAML files):

- `DB_USER`: Database username
- `DB_PASSWORD`: Database password

### 3. Build and Run

Use the Maven Wrapper to build and run the application:

```powershell
# Build the project
.\mvnw clean install

# Run the application with development profile
java -Dspring.profiles.active=development -jar target/url-shortener-0.0.1-SNAPSHOT.jar
```

Alternatively, run directly with Maven:

```powershell
.\mvnw spring-boot:run -Dspring-boot.run.profiles=development
```

The service will be available at `http://localhost:8080`.

## API Endpoints

### URL Operations

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/urls` | Shorten a long URL. |
| `GET` | `/api/urls/{code}` | Retrieve metadata for a short code. |
| `GET` | `/r/{code}` | Resolve and redirect to the original URL. |

#### Example: Shorten a URL
**Request:**
```http
POST /api/urls
Content-Type: application/json

{
  "longUrl": "https://www.example.com/very/long/path/to/resource"
}
```

**Response:**
```json
{
  "code": "aB12cD",
  "shortUrl": "http://localhost:8080/r/aB12cD"
}
```

### Monitoring & Metrics

- **Health Check**: `http://localhost:8080/actuator/health`
- **Prometheus Metrics**: `http://localhost:8080/actuator/prometheus`

## Tech Stack

- **Framework**: Spring Boot 3
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Metrics**: Micrometer / Prometheus
- **Validation**: Jakarta Validation / Apache Commons Validator
- **Mapping**: MapStruct
- **Lombok**: For boilerplate code reduction
